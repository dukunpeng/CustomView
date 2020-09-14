package com.mark.views.views.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.mark.views.ScrollLinearLayoutManager;
import com.mark.views.views.KLine1;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_MOVE;


/**
 * @Author : Mark
 * Date    : 2020/3/9  11:00
 * Desc    :折线图的基类，如果不考虑放大缩小的交互，绘图的速度可以由
 * path.rLineto()优化
 */
public abstract class GraphBaseView<A extends BaseAttribute> extends BaseView implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, IGraph {

    protected final int LONG_PRESS_EVENT = 10;//长按事件的分发

    /**
     * 用来比较大小用，所以取反
     */
    protected final int TEMP_MAXY = Integer.MIN_VALUE;
    protected final int TEMP_MINY = Integer.MAX_VALUE;
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
    protected int LONG_PRESS_TIME = 180;
    //是否长按的状态
    protected boolean isLongPressing;
    //最新的Touch事件
    protected MotionEvent latestEvent;
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
    protected Paint outRectPaint;
    protected A attribute;

    protected int mTouchIndex = -1;

    /**
     * 触摸影响半径
     */
    protected final int TOUCH_RANGE = 5;

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
    protected int[] gridMatrix = new int[]{10, 5};

    /**
     * 网格x轴间距，网格y轴间距
     */
    protected float gridXSpace, gridYSpace;

    protected int gridPaintColor = Color.parseColor("#f6f6f6");
    protected int outRectColor = Color.parseColor("#979797");
    protected int outRectBgColor = Color.parseColor("#FAFAFA");
    /**
     * 四周边距
     */
    protected int paddingLeft, paddingTop, paddingRight, paddingBottom;
    protected Paint linePaint, circlePaint, gridPaint, touchPaint;
    /**
     * 第一个点的x轴坐标
     */
    protected float firstPointX;

    protected Runnable mLongPressRunnable;//用来重新定义长按事件的Runable

    /**
     * 控制缩放灵敏度的阈值 数值越高，越灵敏
     */
   // protected final float scaleLevel = 25f;

    /**
     * x轴总刻度数量
     */
    protected int xCalibrationCount = 0;

    /**
     * 处理所有事务的Handler
     */
    protected Handler mHandler = new Handler() {
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
    protected boolean isScaling;

    private boolean verticalScrollEnable = true;

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


    public void setVerticalScrollEnable(boolean verticalScrollEnable) {
        this.verticalScrollEnable = verticalScrollEnable;
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

    }

    @Override
    public void initParams() {
        initAttribute();
        firstPointX =paddingLeft + leftMargin;
    }

    /**
     * 初始化配置
     */
    protected void initAttribute() {
        attribute = getAttribute();
        this.gridMatrix = attribute.gridMatrix;
        this.gridPaintColor = attribute.gridPaintColor;


        this.paddingLeft = dip2px(attribute.paddings[0]);
        this.paddingTop = dip2px(attribute.paddings[1]);
        this.paddingRight = dip2px(attribute.paddings[2]);
        this.paddingBottom = dip2px(attribute.paddings[3]);

        this.leftMargin = dip2px(attribute.margins[0]);
        this.topMargin = dip2px(attribute.margins[1]);
        this.rightMargin = dip2px(attribute.margins[2]);
        this.bottomMargin = dip2px(attribute.margins[3]);
    }

    protected abstract A getAttribute();

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

        outRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outRectPaint.setColor(outRectColor);
        outRectPaint.setStyle(Paint.Style.STROKE);
        outRectPaint.setStrokeWidth(3f);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(gridPaintColor);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f);
        gridPaint.setAlpha(200);
        gridPaint.setAntiAlias(true);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5},
                1));

    }

    public void configRectFs() {
        graphDrawableRectF.left = paddingLeft;
        graphDrawableRectF.top = paddingTop;
        graphDrawableRectF.right = width - paddingRight;
        graphDrawableRectF.bottom = height - paddingBottom;
        ///
        touchableRectF.left = 0f;
        touchableRectF.right = width;
        touchableRectF.top = 0f;
        touchableRectF.bottom = height;

        outSideRectF.left = graphDrawableRectF.left;
        outSideRectF.right = graphDrawableRectF.right;
        outSideRectF.top = graphDrawableRectF.top;
        outSideRectF.bottom = graphDrawableRectF.bottom;

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

    @Override
    public void drawGridLine(Canvas canvas) {
        if (isVerticalGridTrue()) {
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
        if (isHorizonGridTrue()) {
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
    }

    @Override
    public void drawLines(Canvas canvas) {

    }

    @Override
    public BaseAttribute buildAttribute() {
        return null;
    }

    @Override
    public void drawOutRect(Canvas canvas) {
        if (attribute != null && isNeedOutRect()) {
            canvas.drawRect(outSideRectF, outRectPaint);
        }
    }

    @Override
    public void drawTouch(Canvas canvas) {
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
        float x = getXbyIndex(mTouchIndex);
        canvas.drawLine(x, graphDrawableRectF.bottom, x, graphDrawableRectF.top, touchPaint);

    }

    protected abstract void drawTouchView(Canvas canvas);
    protected abstract void drawText(Canvas canvas);
    protected abstract float getXbyIndex(int mTouchIndex);

    private float mPosY;
    private float mCurPosY;
    private float mPosX;
    private float mCurPosX;
    ScrollLinearLayoutManager manager;

    public void setManager(ScrollLinearLayoutManager manager) {
        this.manager = manager;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();
                if ( (Math.abs(mCurPosY - mPosY) >= 5||Math.abs(mPosX-mCurPosX)>=5)
                        &&( Math.abs(mPosY-mCurPosY)-Math.abs(mPosX-mCurPosX) <0)){
                    getParent().requestDisallowInterceptTouchEvent(!verticalScrollEnable);
                    if (manager!=null){
                        manager.setCanVerticalScroll(verticalScrollEnable);
                    }
                    return /*!verticalScrollEnable||*/super.dispatchTouchEvent(event);
                }else{
                   /* if (manager!=null){
                        manager.setCanVerticalScroll(!verticalScrollEnable);
                    }*/
                    getParent().requestDisallowInterceptTouchEvent(verticalScrollEnable);
                    return super.dispatchTouchEvent(event);
                }
        }

        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, String.format("onTouchEvent: %d", event.getAction()));
        if (!isNeedTouch()){
            return false;
        }
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
        if (manager!=null){
            manager.setCanVerticalScroll(true);
        }
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
    public void calculateByUpdate() {
        updateY();
        calculateAll();
        calculateGrid();
        filter().invalidate();
    }

    @Override
    public boolean isNeedGrid() {
        return attribute.isNeedGrid;
    }

    @Override
    public boolean isHorizonGridTrue() {
        return attribute.isHorizonGridTrue;
    }

    @Override
    public boolean isVerticalGridTrue() {
        return attribute.isVerticalGridTrue;
    }

    @Override
    public boolean isNeedOutRect() {
        return attribute.isNeedOutRect;
    }

    @Override
    public boolean isNeedTouch() {
        return attribute.isNeedTouch;
    }

    @Override
    public boolean isNeedScroll() {
        return attribute.isNeedScroll;
    }

    @Override
    public boolean isBézier() {
        return attribute.isBézier;
    }

    /*   */

    /**
     * 计算数值
     *//*
    public void calculateAll() {
        if (mDatas != null && mDatas.get(0) != null) {
            xCalibrationCount = mDatas.get(0).points().size();
        } else {
            xCalibrationCount = 0;
        }

    }*/
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

    public float getGraphDrawableRectFWidth(){
        return graphDrawableRectF.width();
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
    protected abstract float maxY();

    /**
     * 获取最小的Y轴值
     *
     * @return
     */
    protected abstract float minY();

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
            //firstPointX = scrollX + graphDrawableRectF.left;
            onGraphScroll();
            invalidate();
        }
        return false;
    }
    protected abstract void onGraphScroll();
    protected abstract float  firstIntervalX();

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
            // if (mDatas == null || mDatas.size() <= 0) return false;
            mTouchIndex = getIndexByX(e2.getX());

            invalidate();
            return true;
        }
        return true;
    }


    /**
     * 二分查找下标
     *
     * @param low
     * @param high
     * @param key
     * @return
     */
    protected int binarySearch(int low, int high, float key) {
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


    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        releaseTouch();
        return false;
    }
    float currentFactor = 1f;
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // if (mDatas == null || mDatas.get(0) == null || mDatas.size() <= 0) return false;

        beforeScale(detector);
        float factor = detector.getScaleFactor();
        currentFactor *= factor;
        currentFactor = Math.max(1.0f, Math.min(currentFactor, 10.0f));
        Log.i(TAG,"factor:"+factor);
        Log.i(TAG,"currentFactor:"+currentFactor);

        //float incremental = scaleLevel*currentFactor;
        /*if (currentFactor > 1) {
            incremental = scaleLevel * currentFactor;
        } else if (currentFactor < 1) {
            incremental = scaleLevel * (currentFactor - 2f);
        }*/
        currentIntervalX = firstIntervalX()*currentFactor;
        Log.i(TAG,"currentIntervalX:"+currentIntervalX);
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
        return attribute.isNeedScale;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    //上一次图x轴步距
    protected float lastIntervalX;

    /**
     * 矫正scrolX前 记录一下位置信息
     *
     * @param detector
     */
    private void beforeScale(ScaleGestureDetector detector) {
        lastIntervalX = currentIntervalX;
    }

    protected void dealScaleDetector(ScaleGestureDetector detector) {

        float l_pre_d = detector.getFocusX() - firstPointX;
        float l_end_d = firstPointX + (xCalibrationCount - 1) * lastIntervalX - detector.getFocusX();

        //本该移动的距离
        float distance = (xCalibrationCount - 1) * (lastIntervalX - currentIntervalX);
        //这里矫正scrollX
        scrollX += distance * Math.abs((detector.getFocusX() - scrollX) / ((xCalibrationCount - 1) * currentIntervalX));
        reLimitScrollX();
    }

    /**
     * 对scrollx做限制，此处应当和onScroll里面的限制规则统一
     */
    protected void reLimitScrollX() {
        if (scrollX > (graphDrawableRectF.left + leftMargin)) {
            scrollX = graphDrawableRectF.left + leftMargin;
        }
        firstPointX = scrollX + graphDrawableRectF.left + leftMargin;
    }

    protected void reLimitIntervalX() {
        if (currentIntervalX > width) {
            currentIntervalX = width;
        }
        if (currentIntervalX < DIP_5) {
            currentIntervalX = DIP_5;
        }
    }


}
