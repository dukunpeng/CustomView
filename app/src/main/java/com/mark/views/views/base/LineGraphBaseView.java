package com.mark.views.views.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.mark.views.common.observer.Observer;

import java.util.List;


/**
 * @Author : Mark
 * Date    : 2020/3/9  11:00
 * Desc    :折线图的基类，如果不考虑放大缩小的交互，绘图的速度可以由
 * path.rLineto()优化
 */
public abstract class LineGraphBaseView extends GraphBaseView<GraphAttribute> implements Observer<IRootChartData> {
    protected List<ILine> mDatas;
    /**
     * 数据源
     */
    private IRootChartData data;

    // <editor-fold  desc= "构造">
    public LineGraphBaseView(Context context) {
        super(context);
    }

    public LineGraphBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGraphBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineGraphBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //</editor-fold>

    /**
     * 画线条
     *
     * @param canvas
     */
    @Override
    public void drawLines(Canvas canvas) {
        if (mDatas == null || mDatas.size() <= 0) return;
        for (ILine line : mDatas) {
            final List<IPoint> points = line.points();
            Path path = new Path();
            Path gradientPath = new Path();
            linePaint.setColor(line.color());
            float radius = linePaint.getStrokeWidth() + DIP_2;
            circlePaint.setColor(Color.parseColor(line.colorHexString().replace("#", "#bb")));
            for (int i = 0; i < points.size(); i++) {
                IPoint point = points.get(i);
                //此条线只有一个点，这里做绘制圆点的处理
                if (points.size() == 1) {
                    canvas.drawCircle(point.pointF().x, point.pointF().y, radius, linePaint);
                    break;
                }
                if (line.isShowPoints()) {
                    canvas.drawCircle(point.pointF().x, point.pointF().y, radius, circlePaint);
                }
                if (i == 0) {
                    path.moveTo(point.pointF().x, point.pointF().y);
                }

                if (attribute.isBézier) {
                    if (i == points.size() - 1) {
                        break;
                    }
                    PointF startp = point.pointF();
                    PointF endp = points.get(i + 1).pointF();
                    float wt = (startp.x + endp.x) / 2;
                    PointF p3 = new PointF();
                    PointF p4 = new PointF();
                    p3.y = startp.y;
//                      p3.x = startp.x + (endp.x - startp.x) * t;
                    p3.x = wt;
                    p4.y = endp.y;
//                      p4.x = startp.x + (endp.x-startp.x) * (1 - t);
                    p4.x = wt;
                    path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
                } else {

                    path.lineTo(point.pointF().x, point.pointF().y);
                }

            }
            canvas.drawPath(path, linePaint);
            drawGradient(canvas, line, points, path, gradientPath);
        }
        //将折线超出x轴坐标的部分截取掉（左边）
        mBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, graphDrawableRectF.left, height);
        canvas.drawRect(rectF, mBgPaint);
        //将折线超出x轴坐标的部分截取掉（右边）
        RectF rectF2 = new RectF(graphDrawableRectF.right, 0, width, height);
        canvas.drawRect(rectF2, mBgPaint);
    }

    /**
     * 画渐变
     *
     * @param canvas
     */
    private void drawGradient(Canvas canvas, ILine line, List<IPoint> points, Path path, Path gradientPath) {
        if (attribute.isNeedGradient) {
            if (points != null) {
                IPoint p = points.get(points.size() - 1);
                //最后一个点
                gradientPath.moveTo(p.pointF().x, p.pointF().y);
                //x轴最后一个点
                gradientPath.lineTo(p.pointF().x, graphDrawableRectF.bottom);
                //x轴第一个点
                gradientPath.lineTo(points.get(0).pointF().x, graphDrawableRectF.bottom);
                //第一个点
                gradientPath.lineTo(points.get(0).pointF().x, points.get(0).pointF().y);
                //跟图形结合
                gradientPath.addPath(path);

                Paint spaint = new Paint();

                LinearGradient mLinearGradient = new LinearGradient(0, 0,
                        0, getHeight(), Color.parseColor("#60"
                        + line.colorHexString().replace("#", "")),
                        Color.parseColor("#00"
                                + line.colorHexString().replace("#", "")),
                        Shader.TileMode.CLAMP);
                spaint.setShader(mLinearGradient);
                spaint.setAntiAlias(true);
                spaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawPath(gradientPath, spaint);
            }
        }
    }


    @Override
    public void update(IRootChartData rootChartData, int type) {
        this.data = rootChartData;
        if (data != null) {
            mDatas = data.lines();
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
            xCalibrationCount = mDatas.get(0).points().size();
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
        for (ILine line : mDatas) {
            //给Line计算位置信息
            float newYPixels = 0;
            float newXPixels = 0;
            int count = 0;
            for (IPoint point : line.points()) {
                float yPercent = (point.valueY() - minY) / dis_h;
                //数据点x坐标
                newXPixels = newXPixels(point,count);
                //数据点y坐标
                newYPixels = height - paddingBottom
                        - (useageHeight * yPercent);
                point.pointF().set(newXPixels, newYPixels);
                count++;
            }

        }
        return this;
    }

    @Override
    protected float firstIntervalX() {
        return intervalX;
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
            for (ILine line : mDatas) {
                for (IPoint point : line.points()) {
                    if (maxY < point.valueY()) {
                        maxY = point.valueY();
                    }
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
            for (ILine line : mDatas) {
                for (IPoint point : line.points()) {
                    if (minY > point.valueY()) {
                        minY = point.valueY();
                    }
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
        List<IPoint> list = mDatas.get(0).points();
        if (x >= firstPointX + (list.size() - 1) * currentIntervalX) {
            return mDatas.get(0).points().size() - 1;
        }
        return binarySearch(0, list.size(), x);

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
        List<IPoint> list = mDatas.get(0).points();
        if (mid < 1) {
            return 0;
        }
        if (mid > list.size() - 2) {
            return 0;
        }

        IPoint point1 = list.get(mid - 1);
        IPoint point2 = list.get(mid);
        IPoint point3 = list.get(mid + 1);
        float d_1 = point2.pointF().x - point1.pointF().x;
        float d_2 = point3.pointF().x - point2.pointF().x;
        if (point2.pointF().x - d_1 / 2 > key) {
            return -1;
        } else if (key >= point2.pointF().x + d_2 / 2) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected void onGraphScroll() {

    }

    protected float newXPixels(IPoint point,int count){
     return  firstPointX + count * currentIntervalX;
    }

    @Override
    protected float getXbyIndex(int mTouchIndex) {
        return firstPointX + mTouchIndex * currentIntervalX;
    }
}
