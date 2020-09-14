package com.mark.views.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.mark.views.views.base.GraphAttribute;
import com.mark.views.views.base.LineGraphBaseView;
import com.mark.views.views.base.IPoint;

/**
 * @Author : Mark
 * Date    : 2020/7/7  17:56
 * Desc    :测试图案
 */
public class LineGraph1 extends LineGraphBaseView {

    private Paint xyLinePaint;

    private Paint textPaintX,textPaintY,touchPaint;

    public LineGraph1(Context context) {
        super(context);
    }

    public LineGraph1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGraph1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineGraph1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected GraphAttribute getAttribute() {
        return    new GraphAttribute.GraphAttributeBuilder() .setGrid(true,false,false)
                .setArrays(new int[]{30,10,10,20},new int[]{0,0,0,0},new int[]{18,6})
                .setPermissions(false,true,true,true)
                .setNeedGradient(true)
                .setBézier(true).build();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXY(canvas);
        drawText(canvas);
    }

    @Override
    public void initPaint() {
        super.initPaint();
        xyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xyLinePaint.setColor(Color.DKGRAY);
        xyLinePaint.setStyle(Paint.Style.STROKE);
        xyLinePaint.setStrokeWidth(2f);

        textPaintX = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintX.setColor(Color.DKGRAY);
        textPaintX.setStyle(Paint.Style.STROKE);
        textPaintX.setStrokeWidth(2f);
        textPaintX.setTextSize(DIP_10);

        textPaintY = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintY.setColor(Color.DKGRAY);
        textPaintY.setStyle(Paint.Style.STROKE);
        textPaintY.setStrokeWidth(2f);
        textPaintY.setTextSize(DIP_10);
    }

    private void drawXY(Canvas canvas){
        canvas.drawLine(graphDrawableRectF.left,graphDrawableRectF.bottom,graphDrawableRectF.right,graphDrawableRectF.bottom,xyLinePaint);
        canvas.drawLine(graphDrawableRectF.left,graphDrawableRectF.bottom,graphDrawableRectF.left,graphDrawableRectF.top,xyLinePaint);
    }

    @Override
    protected void drawText(Canvas canvas) {
        if (mDatas==null||mDatas.size()<=0){
            return;
        }
        //画x轴文字
        for (int i = 0; i <mDatas.get(0).points().size() ; i++) {
            IPoint point = mDatas.get(0).points().get(i);
            String value = point.key();
            float x = 0;
            if (i==0){
                x = +firstPointX+i* currentIntervalX;
            }else{
               x  = -getTextWidth(textPaintX,value)/2f+firstPointX+i* currentIntervalX;
            }
            canvas.drawText(value,x,graphDrawableRectF.bottom+getTextHeight(textPaintX,value)+DIP_5,textPaintX);


        }
        RectF rectF = new RectF(0, graphDrawableRectF.bottom, graphDrawableRectF.left, height);
        canvas.drawRect(rectF, mBgPaint);
        RectF rectF2 = new RectF(graphDrawableRectF.right, graphDrawableRectF.bottom, width, height);
        canvas.drawRect(rectF2, mBgPaint);
        //画y轴刻度文字
        if (yCalibrationCount>1){
            float yStep = graphDrawableRectF.height()/(yCalibrationCount-1);
            for (int i = 0; i < yCalibrationCount; i++) {
                String value = String.valueOf(minY+yCalibrationStepDistance*i);
                canvas.drawText(value,-getTextWidth(textPaintY,value)+paddingLeft-DIP_2,graphDrawableRectF.bottom-yStep*i+getTextHeight(textPaintY,value)/2,textPaintY);

            }
        }

    }

    @Override
    protected void drawTouchView(Canvas canvas) {

    }

    @Override
    public void onReleaseTouch() {

    }
}
