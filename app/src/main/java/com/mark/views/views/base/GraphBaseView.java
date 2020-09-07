package com.mark.views.views.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
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

import static android.view.MotionEvent.ACTION_CANCEL;


/**
 * @Author : Mark
 * Date    : 2020/3/9  11:00
 * Desc    :折线图的基类，如果不考虑放大缩小的交互，绘图的速度可以由
 * path.rLineto()优化
 */
public abstract class GraphBaseView extends BaseView implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, Observer<IRootChartData> {

    protected final int LONG_PRESS_EVENT = 10;//长按事件的分发

    /**
     * 用来比较大小用，所以取反
     */
    private final int TEMP_MAXY = Integer.MIN_VALUE;
    private final int TEMP_MINY = Integer.MAX_VALUE;
    /**
     * 控制图形扩展区域的系数，第1个针对最大值，第2个针对最小值
     */
    protected float[] heightRate = new float[]{0.2f, 0f};
    /**
     * 初始默认的最小Y值
     */
    protected float defaultMinY = 0f;
    /**
     * 图形滑动的距离
     */
    protected float scrollX;
    /**
     * 原始x轴步距
     */
    protected float intervalX = 3 * DIP_10;
    /**
     * 当前x轴的步距
     */
    protected float currentIntervalX = intervalX;


    protected GestureDetector gestureDetector; //滑动手势
    protected ScaleGestureDetector scaleGestureDetector; //缩放手势
    protected float maxY, minY;
    //触发时长
    private int LONG_PRESS_TIME = 180;
    //是否长按的状态
    private boolean isLongPressing;
    //最新的Touch事件
    private MotionEvent latestEvent;
    /**
     * 可触摸区域
     */
    protected RectF touchableRectF;
    /**
     * 图形可绘制范围区域
     */
    protected RectF graphDrawableRectF;
    /**
     * 四周边框区域
     */
    protected RectF outSideRectF;
    /**
     * 图形距边框的距离
     */
    protected int leftMargin, rightMargin, topMargin, bottomMargin;
    private Paint outRectPaint;
    private GraphAttribute attribute;

    private int mTouchIndex = -1;

    /**
     * 触摸影响半径
     */
    private final int TOUCH_RANGE = 5;

    /**
     * y轴的刻度数量
     */
    protected int yCalibrationCount = 5;
    /**
     * y轴的刻度步距
     */
    protected float yCalibrationStepDistance = 0;
    /**
     * x,y轴的网格刻度数
     */
    private int[] gridMatrix = new int[]{10, 5};

    /**
     * 网格x轴间距，网格y轴间距
     */
    private float gridXSpace, gridYSpace;

    private int gridPaintColor = Color.parseColor("#f6f6f6");
    /**
     * 四周边距
     */
    protected int paddingLeft = 4 * DIP_10, paddingTop = DIP_10, paddingRight = DIP_10, paddingBottom = 2 * DIP_10;
    private Paint linePaint, circlePaint, gridPaint, touchPaint;
    /**
     * 第一个点的x轴坐标
     */
    protected float firstPointX;

    protected List<ILine> mDatas;
    /**
     * 数据源
     */
    private IRootChartData data;
    private Runnable mLongPressRunnable;//用来重新定义长按事件的Runable

    /**
     * 控制缩放灵敏度的阈值 数值越高，越灵敏
     */
    private final float scaleLevel = 3;

    /**
     * x轴总刻度数量
     */
    private int xCalibrationCount = 0;

    /**
     * 处理所有事务的Handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LONG_PRESS_EVENT:
                    setLongPressing(true);
                    if (latestEvent != null) {
                        gestureDetector.onTouchEvent(latestEvent);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 缩放手势是否在继续
     */
    private boolean isScaling;

    // <editor-fold  desc= "构造">
    public GraphBaseView(Context context) {
        super(context);
    }

    public GraphBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GraphBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //</editor-fold>


    public void setLongPressing(boolean longPressing) {
        isLongPressing = longPressing;
    }

    public void setLatestEvent(MotionEvent latestEvent) {
        this.latestEvent = latestEvent;
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        gestureDetector = new GestureDetector(context, this);
        gestureDetector.setIsLongpressEnabled(false);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        gestureDetector.setIsLongpressEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            scaleGestureDetector.setQuickScaleEnabled(false);
        }
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(mHandler.obtainMessage(LONG_PRESS_EVENT));
            }
        };
        graphDrawableRectF = new RectF();
        touchableRectF = new RectF();
        outSideRectF = new RectF();
        attribute = new GraphAttribute.GraphAttributeBuilder().build();
        firstPointX = graphDrawableRectF.left + leftMargin;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configRectFs();
    }

    @Override
    public void initPaint() {

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(8f);
        //linePaint.setStrokeCap(Paint.Cap.ROUND);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeWidth(8f);

        touchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchPaint.setColor(Color.RED);
        touchPaint.setStyle(Paint.Style.STROKE);
        touchPaint.setStrokeWidth(1f);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(gridPaintColor);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f);
        gridPaint.setAlpha(200);
        gridPaint.setAntiAlias(true);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5},
                1));

    }

    private void configRectFs() {
        graphDrawableRectF.left = paddingLeft;
        graphDrawableRectF.top = paddingTop;
        graphDrawableRectF.right = width - paddingRight;
        graphDrawableRectF.bottom = height - paddingBottom;
        ///
        touchableRectF.left = 0f;
        touchableRectF.right = width;
        touchableRectF.top = 0f;
        touchableRectF.bottom = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateRootData();
        drawOutRect(canvas);
        drawGridLine(canvas);
        drawLines(canvas);
        drawTouch(canvas);
    }

    protected void drawGridLine(Canvas canvas) {
        for (int i = 0; i < gridMatrix[0]; i++) {
            canvas.drawLine(graphDrawableRectF.left + i * gridXSpace, graphDrawableRectF.bottom, graphDrawableRectF.left + i * gridXSpace, graphDrawableRectF.top, gridPaint);
        }
        for (int i = 0; i < gridMatrix[1]; i++) {
            canvas.drawLine(graphDrawableRectF.left, graphDrawableRectF.bottom - i * gridYSpace, graphDrawableRectF.right, graphDrawableRectF.bottom - i * gridYSpace, gridPaint);
        }
    }


    protected abstract void drawText(Canvas canvas);

    protected void drawOutRect(Canvas canvas) {
        if (attribute != null && attribute.isNeedOutRect) {
            canvas.drawRect(outSideRectF, outRectPaint);
        }
    }

    /**
     * 画线条
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {
        if (mDatas == null || mDatas.size() <= 0) return;
        for (ILine line : mDatas) {
            final List<IPoint> points = line.points();
            Path path = new Path();
            Path gradientPath = new Path();
            linePaint.setColor(line.color());
            float radius = linePaint.getStrokeWidth() + DIP_2;
            circlePaint.setColor(Color.parseColor(int2Hex(line.color()).replace("#", "#bb")));
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

    protected void drawTouch(Canvas canvas) {
        if (isLongPressing) {
            drawTouchVerticalLine(canvas);
            drawTouchView(canvas);
        }
    }

    /**
     * 画触摸线
     *
     * @param canvas
     */
    private void drawTouchVerticalLine(Canvas canvas) {
        if (mTouchIndex < 0) return;
        float x = firstPointX + mTouchIndex * currentIntervalX;
        canvas.drawLine(x, graphDrawableRectF.bottom, x, graphDrawableRectF.top, touchPaint);

    }

    protected abstract void drawTouchView(Canvas canvas);

    /**
     * 画渐变
     *
     * @param canvas
     */
    private void drawGradient(Canvas canvas, ILine line, List<IPoint> points, Path path, Path gradientPath) {
        if (attribute.isNeedVerticalLine) {
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
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, String.format("onTouchEvent: %d", event.getAction()));

        //将触摸事件抛出到全局变量
        setLatestEvent(event);
        int pointerCount = event.getPointerCount();

        if (pointerCount >= 2) {
            mHandler.removeCallbacks(mLongPressRunnable);
            //有两个触摸点才能分发缩放手势
            if (2 == pointerCount) {
                return scaleGestureDetector.onTouchEvent(event);
            }
        }
        if (event.getAction() == ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            releaseTouch();
            return super.onTouchEvent(event);
        }
        if (isScaling) {
            return false;
        }
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    /**
     * 手指离开屏幕
     */
    protected void releaseTouch() {
        isScaling = false;
        isLongPressing = false;
        mHandler.removeCallbacks(mLongPressRunnable);
        invalidate();
        onReleaseTouch();
    }

    /**
     * 控件之外的事件
     *
     * @param event
     */
    protected void onOutSideTouch(MotionEvent event) {

    }

    public abstract void onReleaseTouch();

    @Override
    public void update(IRootChartData rootChartData, int type) {
        this.data = rootChartData;
        if (data != null) {
            mDatas = data.lines();
        } else {
            mDatas = null;
        }
        updateY();
        calculateAll();
        filter().invalidate();
    }

    /**
     * 计算数值
     */
    public void calculateAll() {
        if (mDatas != null && mDatas.get(0) != null) {
            xCalibrationCount = mDatas.get(0).points().size();
        } else {
            xCalibrationCount = 0;
        }
        calculateGrid();
    }

    public void updateY() {
        maxY();
        minY();
        reCorrectY();
        yCalibrationStepDistance();
    }

    /**
     * 计算网格线
     */
    public void calculateGrid() {
        gridXSpace = graphDrawableRectF.width() / (gridMatrix[0] - 1);
        gridYSpace = graphDrawableRectF.height() / (gridMatrix[1] - 1);
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
                newXPixels = firstPointX + count * currentIntervalX;
                //数据点y坐标
                newYPixels = height - paddingBottom
                        - (useageHeight * yPercent);
                point.pointF().set(newXPixels, newYPixels);
                count++;
            }

        }
        return this;
    }

    /**
     * 对数据源做过滤操作
     *
     * @return
     */
    @Override
    public BaseView filter() {
        return this;
    }

    protected float currentIntervalX() {
        return currentIntervalX;
    }

    protected float getYCalibrationStepDistance() {
        return yCalibrationStepDistance;
    }

    protected void yCalibrationStepDistance() {
        if (maxY - minY > 0) {
            if (yCalibrationCount > 1) {
                yCalibrationStepDistance = (maxY - minY) / (yCalibrationCount - 1);
            } else {
                yCalibrationStepDistance = maxY - minY;
            }
        }
    }

    /**
     * 获取最大的Y轴值
     *
     * @return
     */
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

    protected boolean setMaxY() {
        return false;
    }

    protected boolean setMinY() {
        return false;
    }

    /**
     * 修正Y值
     */
    private void reCorrectY() {
        float distance = maxY - minY;
        if (distance == 0) {
            maxY += 10;
            minY -= 10;
        } else {
            maxY += distance * heightRate[0];
            minY -= distance * heightRate[1];
        }
        Log.i(TAG, String.format("maxY = %f  minY = %f", maxY, minY));
    }


    /**
     * 获取参与计算图形分布的宽度
     *
     * @return
     */
    protected float getUseageWidth() {
        return graphDrawableRectF.width() - leftMargin - rightMargin;
    }

    /**
     * 获取参与计算图形分布的高度
     *
     * @return
     */
    protected float getUseageHeight() {
       /* if (graphDrawableRectF==null){
            return 0;
        }*/
        return graphDrawableRectF.height() - topMargin - bottomMargin;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mHandler.postDelayed(mLongPressRunnable, LONG_PRESS_TIME);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isLongPressing) {
            return onLongScroll(e1, e2, distanceX, distanceY);
        }
        if (e2.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(distanceX) > TOUCH_RANGE
                    || Math.abs(distanceY) > TOUCH_RANGE) {
                mHandler.removeCallbacks(mLongPressRunnable);
            }
        }

        if (!attribute.isNeedScroll) {
            return false;
        }
        if (e2.getX() >= touchableRectF.left && e2.getX() <= touchableRectF.right) {
            scrollX += -distanceX;
            reLimitScrollX();
            firstPointX = scrollX + graphDrawableRectF.left;
            invalidate();
        }
        return false;
    }

    /**
     * 长按后的滑动
     *
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     * @return
     */
    public boolean onLongScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if ((e2.getAction() == MotionEvent.ACTION_MOVE || e2
                .getAction() == MotionEvent.ACTION_DOWN)) {
            if (mDatas == null || mDatas.size() <= 0) return false;
            mTouchIndex = getIndexByX(e2.getX());

            invalidate();
            return true;
        }
        return true;
    }

    private int getIndexByX(float x) {
        if (x <= firstPointX) {
            return 0;
        }
        List<IPoint> list = mDatas.get(0).points();
        if (x >= firstPointX + list.size() * currentIntervalX) {
            return mDatas.get(0).points().size() - 1;
        }
        return binarySearch(0, list.size(), x);

    }

    /**
     * 二分查找下标
     *
     * @param low
     * @param high
     * @param key
     * @return
     */
    private int binarySearch(int low, int high, float key) {
        if (low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (mid <= 0) {
            return 0;
        }
        int result = check(key, mid);
        if (result == 0) {
            return mid;
        } else if (result == -1) {
            return binarySearch(low, mid + result, key);
        } else {
            return binarySearch(low + result, high, key);
        }
    }

    /**
     * 检测是否匹配上
     *
     * @param key
     * @param mid
     * @return -1 代表key 小 1 代表key大  0代表key合适
     */
    private int check(float key, int mid) {
        List<IPoint> list = mDatas.get(0).points();
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
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        releaseTouch();
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mDatas == null || mDatas.get(0) == null || mDatas.size() <= 0) return false;
        beforeScale(detector);
        float factor = detector.getScaleFactor();
        float incremental = 0f;
        if (factor > 1) {
            incremental = scaleLevel * factor;
        } else if (factor < 1) {
            incremental = scaleLevel * (factor - 2);
        }
        currentIntervalX += incremental;
        reLimitIntervalX();
        dealScaleDetector(detector);
        invalidate();
        //是否重置缩放因子
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        isScaling = true;
        //是否开启缩放
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    //上一次图x轴步距
    private float lastIntervalX;

    /**
     * 矫正scrolX前 记录一下位置信息
     *
     * @param detector
     */
    private void beforeScale(ScaleGestureDetector detector) {
        lastIntervalX = currentIntervalX;
    }

    private void dealScaleDetector(ScaleGestureDetector detector) {

        float l_pre_d = detector.getFocusX() - firstPointX;
        float l_end_d = firstPointX + (xCalibrationCount - 1) *lastIntervalX -detector.getFocusX();

        //本该移动的距离
        float distance = (xCalibrationCount - 1) * (lastIntervalX - currentIntervalX);
        //这里矫正scrollX
        scrollX += distance * Math.abs((detector.getFocusX() - scrollX) / ((xCalibrationCount - 1) * currentIntervalX));
        reLimitScrollX();
    }

    /**
     * 对scrollx做限制，此处应当和onScroll里面的限制规则统一
     */
    private void reLimitScrollX() {
        if (scrollX > (graphDrawableRectF.left + leftMargin)) {
            scrollX = graphDrawableRectF.left + leftMargin;
        }
        firstPointX = scrollX + graphDrawableRectF.left + leftMargin;
    }

    private void reLimitIntervalX() {
        if (currentIntervalX > width) {
            currentIntervalX = width;
        }
        if (currentIntervalX < DIP_5) {
            currentIntervalX = DIP_5;
        }
    }


}
