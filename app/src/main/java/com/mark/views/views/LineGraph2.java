package com.mark.views.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.mark.views.DateUtil;
import com.mark.views.views.base.GraphAttribute;
import com.mark.views.views.base.ILine;
import com.mark.views.views.base.IPoint;
import com.mark.views.views.base.LineGraphBaseView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author : Mark
 * Date    : 2020/7/7  17:56
 * Desc    :测试图案
 */
public class LineGraph2 extends LineGraphBaseView {

    private Paint touchTextPaint;

    private Paint textPaintX, textPaintY, touchRectPaint, titlePaint, shortLinePaint;

    private long startTime, endTime, during;

    private final long step = 30 * 60 * 1000L;
    private final int xStringCount = 17;

    public LineGraph2(Context context) {
        super(context);
    }

    public LineGraph2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGraph2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineGraph2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void initParams() {
        super.initParams();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        endTime = calendar.getTimeInMillis();

        during = endTime - startTime;
    }

    @Override
    protected GraphAttribute getAttribute() {
        return new GraphAttribute.GraphAttributeBuilder().setGrid(true, false, true)
                .setArrays(new int[]{10, 30, 10, 36}, new int[]{0, 0, 0, 0}, new int[]{xStringCount + 1, 4})
                .setPermissions(true, true, false, true)
                .setNeedGradient(false)
                .setyCalibrationCount(4)
                .setGridPaintColor(Color.parseColor("#e6e6e6"))
                .setBézier(false).build();
    }
    @Override
    public void drawOutRect(Canvas canvas) {
        outRectPaint.setStyle(Paint.Style.FILL);
        outRectPaint.setColor(outRectBgColor);
        canvas.drawRect(outSideRectF,outRectPaint);
        outRectPaint.setColor(outRectColor);
        outRectPaint.setStyle(Paint.Style.STROKE);
        super.drawOutRect(canvas);
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
        touchTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchTextPaint.setColor(Color.DKGRAY);
        touchTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        touchTextPaint.setStrokeWidth(2f);
        touchTextPaint.setTextSize(DIP_10);

        linePaint.setStrokeWidth(DIP_2);

        shortLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shortLinePaint.setColor(Color.DKGRAY);
        shortLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shortLinePaint.setStrokeWidth(5f);
        shortLinePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaintX = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintX.setColor(Color.parseColor("#7E7E86"));
        textPaintX.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaintX.setStrokeWidth(1f);
        textPaintX.setTextSize(DIP_9);

        textPaintY = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintY.setColor(Color.DKGRAY);
        textPaintY.setStyle(Paint.Style.FILL);
        textPaintY.setStrokeWidth(2f);
        textPaintY.setTextSize(DIP_10);

        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#7E7E86"));
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setStrokeWidth(3f);
        titlePaint.setTextSize(2 * DIP_6);


        touchRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchRectPaint.setColor(Color.parseColor("#abDEEAF7"));
        touchRectPaint.setStyle(Paint.Style.FILL);

        touchPaint.setColor(Color.parseColor("#AEAEAE"));
    }

    private void drawXY(Canvas canvas) {
/*
        canvas.drawLine(graphDrawableRectF.left, graphDrawableRectF.bottom, graphDrawableRectF.right, graphDrawableRectF.bottom, touchTextPaint);
        canvas.drawLine(graphDrawableRectF.left, graphDrawableRectF.bottom, graphDrawableRectF.left, graphDrawableRectF.top, touchTextPaint);
*/
    }

    @Override
    protected void drawText(Canvas canvas) {
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        float d = outSideRectF.width() / xStringCount;
        //画x轴文字
        int count = 0;
        for (int i = 0; i < xStringCount; i++) {
            count = canvas.save();
            String value = DateUtil.getDateFormat(startTime + (i+1) * step, DateUtil.sequenceFormat3);
            float x = outSideRectF.left + (i) * d + getTextHeight(textPaintX, value) / 1.414f;
            canvas.rotate(-45, x, graphDrawableRectF.bottom + paddingBottom * 3 / 4);
            canvas.drawText(value, x, graphDrawableRectF.bottom + paddingBottom * 3 / 4, textPaintX);
            canvas.restoreToCount(count);

        }
        RectF rectF = new RectF(0, graphDrawableRectF.bottom, graphDrawableRectF.left, height);
        canvas.drawRect(rectF, mBgPaint);
        RectF rectF2 = new RectF(graphDrawableRectF.right + DIP_3, graphDrawableRectF.bottom, width, height);
        canvas.drawRect(rectF2, mBgPaint);
        //画y轴刻度文字
        if (yCalibrationCount > 1) {
            float yStep = graphDrawableRectF.height() / (yCalibrationCount - 1);
            for (int i = 0; i < yCalibrationCount; i++) {

                if (i == 0 || i == yCalibrationCount - 1) {
                    continue;
                }

                String value = String.valueOf(minY + yCalibrationStepDistance * i);
                canvas.drawText(value, +paddingLeft + DIP_2, graphDrawableRectF.bottom - yStep * i + getTextHeight(textPaintY, value) / 2, textPaintY);

            }
        }
        //画title
        float startX = outSideRectF.left;
        for (ILine line : mDatas) {
            canvas.drawText(line.name(), startX, outSideRectF.top - DIP_7, titlePaint);
            shortLinePaint.setColor(line.color());
            float endX = startX+getTextWidth(titlePaint, line.name());
            canvas.drawLine(startX, outSideRectF.top - DIP_4, endX, outSideRectF.top - DIP_4, shortLinePaint);
            startX= endX+DIP_5;
        }

    }

    IPoint p1, p2, p3;

    @Override
    protected void drawTouchView(Canvas canvas) {

        if (mDatas == null) return;
        if (mTouchIndex < 0) return;
        float x = mDatas.get(0).points().get(mTouchIndex).pointF().x;
        float y = mDatas.get(0).points().get(mTouchIndex).pointF().y;

        String value1 = String.format("%s:%s","Time", DateUtil.getDateFormat(Long.parseLong(mDatas.get(0).points().get(mTouchIndex).valueYString()),DateUtil.sequenceFormat3));
        String value2 = "", value3 = "";

        for (ILine line : mDatas) {
            shortLinePaint.setColor(line.color());
            canvas.drawCircle( line.points().get(mTouchIndex).pointF().x, line.points().get(mTouchIndex).pointF().y,DIP_2,shortLinePaint);
            shortLinePaint.setColor(Color.parseColor(line.colorHexString().replace("#","#30")));
            canvas.drawCircle( line.points().get(mTouchIndex).pointF().x, line.points().get(mTouchIndex).pointF().y,DIP_6,shortLinePaint);
            if (line.id() == 1) {
                p1 = line.points().get(mTouchIndex);
            } else if (line.id() == 2) {
                p2 = line.points().get(mTouchIndex);
            } else if (line.id() == 3) {
                p3 = line.points().get(mTouchIndex);
                value2 = String.format("%s:%s", line.name(), p3.valueYString());
            }
        }
        value3 = String.format("%s:%s", mDatas.get(0).name()+"/"+mDatas.get(1).name(), p1.valueYString()+"/"+p2.valueYString());
        RectF rectF = new RectF();
        int maxtextWidth = 0;
        int textHeight = 0;
        int w1 = getTextWidth(touchTextPaint, value1);
        int w2 = getTextWidth(touchTextPaint, value2);
        int w3 = getTextWidth(touchTextPaint, value3);
        textHeight = getTextHeight(touchTextPaint, value3);
        maxtextWidth = Math.max(w1, w2);
        maxtextWidth = Math.max(maxtextWidth, w3);
        int space = DIP_10;
        int padding = DIP_10;

        if (x - outSideRectF.left > outSideRectF.width() / 2) {
            rectF.left = x - space - maxtextWidth - 2 * padding;
            rectF.top = outSideRectF.top+outSideRectF.height()*1/3 ;
            rectF.right = x - space;
            rectF.bottom = rectF.top + (textHeight + DIP_5) * 3-DIP_5+ 2 * padding;

        } else {
            rectF.left = x + DIP_10;
            rectF.top = outSideRectF.top+outSideRectF.height()*1/3 ;
            rectF.right = x + space + maxtextWidth + 2 * padding;
            rectF.bottom =  rectF.top + (textHeight + DIP_5) * 3-DIP_5+ 2 * padding;
        }
        canvas.drawRect(rectF, touchRectPaint);
        canvas.drawText(value1, rectF.left + padding, rectF.top + padding + (textHeight ), touchTextPaint);
        canvas.drawText(value2, rectF.left + padding, rectF.top + padding + (textHeight + DIP_5)*2 -DIP_5, touchTextPaint);
        canvas.drawText(value3, rectF.left + padding, rectF.top + padding + (textHeight + DIP_5) * 3-DIP_5, touchTextPaint);

    }

    @Override
    public void onReleaseTouch() {

    }

    @Override
    protected float newXPixels(IPoint point, int count) {
        BigDecimal decimal1 = new BigDecimal(String.valueOf(Long.parseLong(point.valueYString()) - startTime));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(during));


        float xPercent = decimal1.divide(decimal2, 6, RoundingMode.UP).floatValue();

        float x = graphDrawableRectF.left  + getGraphDrawableRectFWidth() * xPercent;
       // Log.i(TAG, "x=" + x + "  count=" + count + "  xPercent=" + xPercent + "  ttt:" + (Long.parseLong(point.valueYString()) - startTime) + " during:" + during);

        return x;
    }

    @Override
    protected float getXbyIndex(int mTouchIndex) {
        return mDatas.get(0).points().get(mTouchIndex).pointF().x;
    }
}
