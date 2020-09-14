package com.mark.views.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.mark.views.views.base.BaseView;
import com.mark.views.views.base.ILine;
import com.mark.views.views.base.IPoint;
import com.mark.views.views.base.KGraphAttribute;
import com.mark.views.views.base.KGraphBaseView;
import com.mark.views.views.base.KPointModel;
import com.mark.views.views.base.Line;
import com.mark.views.views.base.LinePoint;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Mark
 * Date    : 2020/9/7  17:03
 * Desc    :
 */
public class KLine2 extends KGraphBaseView {

    private DecimalFormat format ;

    private Paint shortLinePaint;

    private Paint textPaintX,textPaintY,titlePaint,maPaint;

    protected List<ILine> lines;
    private static int[] maColors = {
            Color.parseColor("#EA4949"),
            Color.parseColor("#FEC200"),
            Color.parseColor("#3897F1"),
            Color.parseColor("#CD65C7"),
    };

    /**
     * 5日10日20日均线权限
     */
    private  boolean[] permissions = {true,true,true};
    /**
     * 可视页面的左右两边对应数据的下标
     */
    private int startIndex,endIndex;
    private  int[] mas = {5,10,20};
    private   String[] maStrings = new String[mas.length+1];

    private int outRectColor = Color.parseColor("#979797");
    private int outRectBgColor = Color.parseColor("#FAFAFA");

    public KLine2(Context context) {
        super(context);
    }

    public KLine2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KLine2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KLine2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    @Override
    public void initParams() {
        super.initParams();
        heightRate = new float[]{0.2f,0.1f};
        format = new DecimalFormat("0.0000");
        format.setRoundingMode(RoundingMode.HALF_UP);
        lines = new ArrayList<>();
        for (int i = 0; i < mas.length+1 ; i++) {
            if (i==mas.length){
                maStrings[i] = "Central Parity";
            }else {
                maStrings[i] = "MA"+mas[i];
            }
        }
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i]){
                Line line = new Line(maColors[i]
                        ,maStrings[i],false,i,null);
                lines.add(line);
            }
        }
    }

    @Override
    protected KGraphAttribute getAttribute() {
        return new KGraphAttribute.KGraphAttributeBuilder()
                .setGrid(true,false,true)
                .setArrays(new int[]{10,10,10,20},new int[]{0,0,0,0},new int[]{18,4})
                .setPermissions(false,true,true,true)
                .setNumbers(7,50,DIP_2,3.0f)
                .setBézier(true)
                .build();
    }

    @Override
    public void initPaint() {
        super.initPaint();
        shortLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shortLinePaint.setColor(Color.DKGRAY);
        shortLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shortLinePaint.setStrokeWidth(5f);
        shortLinePaint.setStrokeCap(Paint.Cap.ROUND);

        maPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maPaint.setColor(Color.DKGRAY);
        maPaint.setStyle(Paint.Style.STROKE);
        maPaint.setStrokeWidth(2f);
        maPaint.setStrokeCap(Paint.Cap.ROUND);

        outRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outRectPaint.setColor(outRectColor);
        outRectPaint.setStyle(Paint.Style.STROKE);
        outRectPaint.setStrokeWidth(3f);

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
        titlePaint.setTextSize(2*DIP_6);

        touchPaint.setColor(Color.parseColor("#AEAEAE"));


    }

    @Override
    protected void drawText(Canvas canvas) {
        if (mDatas==null||mDatas.size()<=0){
            return;
        }
        //画x轴文字
        String value = mDatas.get(startIndex).date();
        canvas.drawText(value,graphDrawableRectF.left,graphDrawableRectF.bottom+getTextHeight(textPaintX,value)+DIP_5,textPaintX);
        value = mDatas.get(endIndex).date();
        canvas.drawText(value,graphDrawableRectF.right - getTextWidth(textPaintX,value),graphDrawableRectF.bottom+getTextHeight(textPaintX,value)+DIP_5,textPaintX);

        float x = graphDrawableRectF.left+DIP_2;
            if (mTouchIndex>=0){
            KPointModel model = mDatas.get(mTouchIndex);
                titlePaint.setColor(Color.parseColor("#7E7E86"));
            String title1 = String.format("%s 开盘：%f 收盘：%f 最高：%f 最低：%f",model.date(),model.start(),model.end(),model.higher(),model.lower());
                canvas.drawText(title1,x,graphDrawableRectF.top+2*DIP_8,titlePaint);
            for (int i = 0; i < maStrings.length ; i++) {
                int color = maColors[i];
                titlePaint.setColor(color);
                canvas.drawText(maStrings[i],x,graphDrawableRectF.top+4*DIP_8,titlePaint);
                int width = getTextWidth(titlePaint,maStrings[i]);
                //canvas.drawLine(x,graphDrawableRectF.top-DIP_4,x+width,graphDrawableRectF.top-DIP_4,shortLinePaint);
                x+=width+DIP_10;
            }
            }

        //画y轴刻度文字
        if (yCalibrationCount>1){
            float yStep = graphDrawableRectF.height()/(yCalibrationCount-1);
            for (int i = 0; i < yCalibrationCount; i++) {
                if (i==0||i==yCalibrationCount-1){
                    continue;
                }
                String value1 = format.format(minY+yCalibrationStepDistance*i);
                canvas.drawText(value1,paddingLeft+DIP_3,graphDrawableRectF.bottom-yStep*i+getTextHeight(textPaintY,value1)/2,textPaintY);

            }
        }

    }

    @Override
    protected void drawTouchView(Canvas canvas) {
        if (mTouchIndex < 0) return;

        KPointModel model = mDatas.get(mTouchIndex);
        if(model.state()==KPointModel.RAISE){
            canvas.drawLine(graphDrawableRectF.left, model.topPointF().y, graphDrawableRectF.right, model.topPointF().y, touchPaint);
        }else {
            canvas.drawLine(graphDrawableRectF.left, model.bottomPointF().y, graphDrawableRectF.right, model.bottomPointF().y, touchPaint);
        }

    }


    @Override
    public void onReleaseTouch() {

    }

    @Override
    protected void onGraphScroll() {
        refreshIndex();
    }
    private void refreshIndex(){
        startIndex = getIndexByX(graphDrawableRectF.left);
        endIndex = getIndexByX(graphDrawableRectF.right);
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
    public BaseView calculateRootData() {
        if (mDatas == null) return this;
        super.calculateRootData();
        //手动算均线值
        for (int i = 0; i < lines.size(); i++) {
            Line line = (Line) lines.get(i);
            int index = line.id();
            int count = mas[index];
            boolean in = true;
            int start = 0;
            float[] arrays = new float[count];
            float[] arrays2 = new float[count];
            line.points().clear();
            while (in){
                int size = count;
                float startX = 0f,endX = 0f;
                for (int j = 0; j < count; j++) {
                    if (start+j>=mDatas.size()-1){
                        in = false;
                        size = j;
                        KPointModel model = mDatas.get(mDatas.size()-1);
                        endX  = model.bottomPointF().x;
                        arrays[j] =  model.end();
                        if (model.state() == KPointModel.RAISE){
                            arrays2[j] =  mDatas.get(mDatas.size()-1).topPointF().y;
                        }else{
                            arrays2[j] =  mDatas.get(mDatas.size()-1).bottomPointF().y;
                        }
                        break;
                    }else {
                        KPointModel model = mDatas.get(start+j);
                        arrays[j] = model.end();
                        if (model.state() == KPointModel.RAISE){
                            arrays2[j] = model.topPointF().y;
                        }else{
                            arrays2[j] =  model.bottomPointF().y;
                        }
                        if (j==0){
                            startX = model.bottomPointF().x;
                        }else if(j==count-1){
                            endX  = model.bottomPointF().x;
                        }
                    }
                }
                start++;
                float avg = avg(arrays,size);
                float avg2 = avg(arrays2,size);
                PointF pointF = new PointF();
                pointF.set((endX+startX)/2f,avg2);
                LinePoint point = new LinePoint("",String.valueOf(avg),avg,0,pointF);
                line.addPoint(point);
            }
        }
        return this;
    }

    @Override
    public void drawLines(Canvas canvas) {
        super.drawLines(canvas);
        for (ILine line : lines) {
            final List<IPoint> points = line.points();
            Path path = new Path();
            maPaint.setColor(line.color());
            float radius = maPaint.getStrokeWidth() ;
           // circlePaint.setColor(Color.parseColor(intToHexValue(line.color()).replace("#", "#bb")));
            for (int i = 0; i < points.size(); i++) {
                IPoint point = points.get(i);
                //此条线只有一个点，这里做绘制圆点的处理
                if (points.size() == 1) {
                    canvas.drawCircle(point.pointF().x, point.pointF().y, radius, maPaint);
                    break;
                }
               /* if (line.isShowPoints()) {
                    canvas.drawCircle(point.pointF().x, point.pointF().y, radius, circlePaint);
                }*/
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
            canvas.drawPath(path, maPaint);
        }
        //将折线超出x轴坐标的部分截取掉（左边）
        mBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, graphDrawableRectF.left, height);
        canvas.drawRect(rectF, mBgPaint);
        //将折线超出x轴坐标的部分截取掉（右边）
        RectF rectF2 = new RectF(graphDrawableRectF.right, 0, width, height);
        canvas.drawRect(rectF2, mBgPaint);
    }

    private float avg(float[] floats, int count){
        float f = 0f;
        for (int i = 0; i <count ; i++) {
            f += floats[i];
        }
        return f/count;
    }

    @Override
    public void calculateByUpdate() {
        if (mTouchIndex==-1){
            mTouchIndex = mDatas.size()-1;
        }
        super.calculateByUpdate();
    }

    @Override
    public void calculateAll() {
        super.calculateAll();
        refreshIndex();
    }
    @Override
    protected void releaseTouch() {
        super.releaseTouch();
        refreshIndex();
    }
}
