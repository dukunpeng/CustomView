package com.mark.views.views.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


/**
 * @Author : Mark
 * Date    : 2020/7/6  13:35
 * Desc    :自定义控件基类，定义了一些基础长度属性变量，事件流的分发，辅助计算方法
 */
public abstract class BaseView extends View implements IBaseView {
    protected final String TAG = getClass().getSimpleName();


    protected Context context;
    // <editor-fold  desc= "基础长度属性变量">
    protected int DIP_1 = dip2px(1);
    protected int DIP_2 = 2 * DIP_1;
    protected int DIP_3 = 3 * DIP_1;
    protected int DIP_4 = 4 * DIP_1;
    protected int DIP_5 = 5 * DIP_1;
    protected int DIP_6 = 6 * DIP_1;
    protected int DIP_7 = 7 * DIP_1;
    protected int DIP_8 = 8 * DIP_1;
    protected int DIP_9 = 9 * DIP_1;
    protected int DIP_10 = 10 * DIP_1;
    //</editor-fold>

    protected Paint mBgPaint;
    protected float width, height;
    /**
     * 绘制背景的区域
     */
    private RectF bgRectF;

    // <editor-fold  desc= "构造">
    public BaseView(Context context) {
        super(context);
        init(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    //</editor-fold>

    private void init(Context context, AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.context = context;
        bgRectF = new RectF();
        view(context, attrs);
        paint();
    }

    private void view(Context context, AttributeSet attrs) {
        initView(context, attrs);
    }

    private void paint() {
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.WHITE);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        bgRectF.left = 0;
        bgRectF.top = 0;
        bgRectF.right = width;
        bgRectF.bottom = height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * 画背景
     *
     * @param canvas
     */
    protected void drawBg(Canvas canvas) {
        canvas.drawRect(bgRectF, mBgPaint);
    }

    // <editor-fold  desc= "辅助计算方法">
    public int dip2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * sp转换px
     */
    public int sp2px(int spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    protected int getTextWidth(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        return (int) paint.measureText(text);
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    protected int getTextHeight(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    // </editor-fold >

    /**
     * Color的Int整型转Color的16进制颜色值
     * colorInt - -12590395
     * return Color的16进制颜色值——#3FE2C5
     */
    public String int2Hex(int colorInt) {
        String hexCode = "";
        hexCode = String.format("#%06X", Integer.valueOf(16777215 & colorInt));
        return hexCode;
    }
}
