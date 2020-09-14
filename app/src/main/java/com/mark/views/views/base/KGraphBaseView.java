package com.mark.views.views.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.mark.views.common.observer.Observer;

import java.util.List;

import static com.mark.views.views.base.KPointModel.DROP;
import static com.mark.views.views.base.KPointModel.LEVEL;
import static com.mark.views.views.base.KPointModel.RAISE;


/**
 * @Author : Mark
 * Date    : 2020/3/9  11:00
 * Desc    :折线图的基类，如果不考虑放大缩小的交互，绘图的速度可以由
 * path.rLineto()优化
 */
public abstract class KGraphBaseView extends GraphBaseView<KGraphAttribute> implements Observer<IRootKChartData> {


    protected List<KPointModel> mDatas;

    /**
     * 数据源
     */
    private IRootKChartData data;


    /**
     * 两个K柱相邻两边的间距，可以是0
     */
    private float barSpace = 2.0f;
    /**
     * K柱宽度
     */
    private float barWidth = 0;

    /**
     * 一屏K柱个数
     */
    private int screenXCalibrationCount = 30;

    private float firstIntervalX = intervalX;


    // <editor-fold  desc= "构造">
    public KGraphBaseView(Context context) {
        super(context);
    }

    public KGraphBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KGraphBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KGraphBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //</editor-fold>

    /**
     * 初始化配置
     */
    @Override
    protected void initAttribute() {
        super.initAttribute();
        this.yCalibrationCount = attribute.yCalibrationCount;
        this.screenXCalibrationCount = attribute.screenXCalibrationCount;
        this.barSpace = attribute.barSpace;
        setVerticalScrollEnable(false);
        setNestedScrollingEnabled(false);
    }

    @Override
    public void configRectFs() {
        super.configRectFs();
        float width = getUseageWidth();
        barWidth = (width - (screenXCalibrationCount - 2) * barSpace) / screenXCalibrationCount;
        Log.i(TAG, "barWidth:" + barWidth);
        firstIntervalX = barSpace+barWidth;
        currentIntervalX = firstIntervalX();
        firstPointX = scrollX + graphDrawableRectF.left + leftMargin + barWidth;
    }

    @Override
    public void initPaint() {
        super.initPaint();
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(attribute.hatchWidth);
    }

    @Override
    protected float firstIntervalX() {
        return firstIntervalX;
    }

    @Override
    protected void dealScaleDetector(ScaleGestureDetector detector) {
        super.dealScaleDetector(detector);
        float f = currentIntervalX/lastIntervalX;
        barWidth *=f;
        barSpace *= f;
    }
    /*    @Override
    public void drawGridLine(Canvas canvas) {
        if (attribute.isVerticalGridTrue) {
            gridPaint.setPathEffect(null);
        } else {
            gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5},
                    1));
        }
        for (int i = 0; i < gridMatrix[0]; i++) {
            if (i == 0 || i == gridMatrix[0] - 1) {
                continue;
            }
            canvas.drawLine(graphDrawableRectF.left + i * gridXSpace, graphDrawableRectF.bottom, graphDrawableRectF.left + i * gridXSpace, graphDrawableRectF.top, gridPaint);
        }
        if (attribute.isHorizonGridTrue) {
            gridPaint.setPathEffect(null);
        } else {
            gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5},
                    1));
        }
        for (int i = 0; i < gridMatrix[1]; i++) {
            if (i == 0 || i == gridMatrix[1] - 1) {
                continue;
            }
            canvas.drawLine(graphDrawableRectF.left, graphDrawableRectF.bottom - i * gridYSpace, graphDrawableRectF.right, graphDrawableRectF.bottom - i * gridYSpace, gridPaint);
        }
    }*/

    /**
     * 画线条
     *
     * @param canvas
     */
    @Override
    public void drawLines(Canvas canvas) {
        if (mDatas == null || mDatas.size() <= 0) return;

        Path raisePath = new Path();
        Path dropPath = new Path();
        Path levelPath = new Path();
        for (KPointModel model : mDatas) {
            switch (model.state()) {
                case RAISE:
                    addPathByKPoint(raisePath, model);
                    break;
                case DROP:
                    addPathByKPoint(dropPath, model);
                    break;
                case LEVEL:
                    addPathByKPoint(levelPath, model);
                    break;
                default:
                    break;
            }
        }
        linePaint.setColor(attribute.raiseColor);
        canvas.drawPath(raisePath, linePaint);
        linePaint.setColor(attribute.dropColor);
        canvas.drawPath(dropPath, linePaint);
        linePaint.setColor(attribute.levelColor);
        canvas.drawPath(levelPath, linePaint);

        //将折线超出x轴坐标的部分截取掉（左边）
        mBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, graphDrawableRectF.left, height);
        canvas.drawRect(rectF, mBgPaint);
        //将折线超出x轴坐标的部分截取掉（右边）
        RectF rectF2 = new RectF(graphDrawableRectF.right, 0, width, height);
        canvas.drawRect(rectF2, mBgPaint);
    }

    private void addPathByKPoint(Path path, KPointModel model) {
        //由下往上绘制
        path.moveTo(model.dropPointF().x, model.dropPointF().y);
        path.lineTo(model.bottomPointF().x, model.bottomPointF().y);
        path.lineTo(model.bottomPointF().x - barWidth / 2f, model.bottomPointF().y);
        path.lineTo(model.bottomPointF().x - barWidth / 2f, model.topPointF().y);
        path.lineTo(model.topPointF().x, model.topPointF().y);
        path.lineTo(model.topPointF().x, model.raisePointF().y);
        path.lineTo(model.topPointF().x, model.topPointF().y);
        path.lineTo(model.topPointF().x + barWidth / 2f, model.topPointF().y);
        path.lineTo(model.topPointF().x + barWidth / 2f, model.bottomPointF().y);
        path.lineTo(model.bottomPointF().x, model.bottomPointF().y);
    }


    @Override
    public void update(IRootKChartData rootChartData, int type) {
        this.data = rootChartData;
        if (data != null) {
            mDatas = data.kPoints();
        } else {
            mDatas = null;
        }
        calculateByUpdate();
    }

    /**
     * 计算数值
     */
    @Override
    public void calculateAll() {
        if (mDatas != null && mDatas.get(0) != null) {
            xCalibrationCount = mDatas.size();
        } else {
            xCalibrationCount = 0;
        }
    }


    /**
     * 计算最新数据
     *
     * @return
     */
    @Override
    public BaseView calculateRootData() {
        if (mDatas == null) return this;
        float useageHeight = getUseageHeight();
        float dis_h = maxY - minY;
        float newYPixels = 0;
        float newXPixels = 0;
        int count = 0;
        for (KPointModel model : mDatas) {

            float bottom = 0f;
            float top = 0f;
            if (model.state() == RAISE) {
                top = model.end();
            } else {
                top = model.start();
            }
            if (model.state() == RAISE) {
                bottom = model.start();
            } else {
                bottom = model.end();
            }
            float yPercent = (bottom - minY) / dis_h;
            newXPixels = firstPointX + count * currentIntervalX;
            //K柱下边
            newYPixels = graphDrawableRectF.bottom
                    - (useageHeight * yPercent);
            model.bottomPointF().set(newXPixels, newYPixels);

            //K柱上边
            yPercent = (top - minY) / dis_h;
            newYPixels = graphDrawableRectF.bottom
                    - (useageHeight * yPercent);
            model.topPointF().set(newXPixels, newYPixels);

            //下影点
            yPercent = (model.lower() - minY) / dis_h;
            newYPixels = graphDrawableRectF.bottom
                    - (useageHeight * yPercent);
            model.dropPointF().set(newXPixels, newYPixels);
            //上影点
            yPercent = (model.higher() - minY) / dis_h;
            newYPixels = height - paddingBottom
                    - (useageHeight * yPercent);
            model.raisePointF().set(newXPixels, newYPixels);
            count++;
        }
        return this;
    }


    /**
     * 获取最大的Y轴值
     *
     * @return
     */
    @Override
    protected float maxY() {
        if (!setMaxY()) {
            float maxY = TEMP_MAXY;
            for (KPointModel model : mDatas) {
                if (maxY < model.maxValue()) {
                    maxY = model.maxValue();
                }

            }
            this.maxY = maxY;
        }
        return maxY;
    }

    /**
     * 获取最小的Y轴值
     *
     * @return
     */
    @Override
    protected float minY() {
        if (!setMinY()) {
            float minY = TEMP_MINY;
            for (KPointModel model : mDatas) {

                if (minY > model.minValue()) {
                    minY = model.minValue();
                }
            }
            this.minY = minY;
        }
        return minY;
    }

    @Override
    public int getIndexByX(float x) {
        if (x <= firstPointX) {
            return 0;
        }
        if (x >= firstPointX + (mDatas.size() - 1) * currentIntervalX) {
            return mDatas.size() - 1;
        }
        return binarySearch(0, mDatas.size(), x);
    }


    /**
     * 检测是否匹配上
     *
     * @param key
     * @param mid
     * @return -1 代表key 小 1 代表key大  0代表key合适
     */
    @Override
    public int check(float key, int mid) {
        if (mid < 1) {
            return 0;
        }
        if (mid > mDatas.size() - 2) {
            return 0;
        }
        KPointModel point1 = mDatas.get(mid - 1);
        KPointModel point2 = mDatas.get(mid);
        KPointModel point3 = mDatas.get(mid + 1);
        float d_1 = point2.bottomPointF().x - point1.bottomPointF().x;
        float d_2 = point3.bottomPointF().x - point2.bottomPointF().x;
        if (point2.bottomPointF().x - d_1 / 2 > key) {
            return -1;
        } else if (key >= point2.bottomPointF().x + d_2 / 2) {
            return 1;
        } else {
            return 0;
        }
    }
    @Override
    protected float getXbyIndex(int mTouchIndex) {
        return firstPointX + mTouchIndex * currentIntervalX;
    }
}
