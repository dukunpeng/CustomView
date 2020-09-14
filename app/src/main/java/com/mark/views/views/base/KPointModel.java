package com.mark.views.views.base;

import android.graphics.PointF;

/**
 * @Author : Mark
 * Date    : 2020/9/7  9:48
 * Desc    :K线图天数据model
 */
public class KPointModel {

    public static final int RAISE = 1,DROP = -1,LEVEL = 0;

    /**
     * 开盘价
     */
    private float start;
    /**
     * 收盘价
     */
    private float end;

    /**
     * 最高点
     */
    private float higher;

    /**
     * 最低点
     */
    private float lower;

    /**
     * 日期
     */
    private String date;
    /**
     * 状态，涨（1）跌（-1）平（0）
     */
    private int state;

    /**
     * 四个数据最大值
     */
    private float max;
    /**
     * 四个数据最小值
     */
    private float min;

    /**
     * K柱底线中心点坐标对应值
     *
     * @return
     */
    private PointF bottomPointF = new PointF();
    /**
     * K柱顶线中心点坐标对应值
     *
     * @return
     */
    private PointF topPointF= new PointF();

    /**
     * 上影线点
     */
    private PointF raisePointF= new PointF();
    /**
     * 下影线点
     */
    private PointF dropPointF= new PointF();

    public KPointModel(float start, float end, float higher, float lower, String date) {
        this.start = start;
        this.end = end;
        this.higher = higher;
        this.lower = lower;
        this.date = date;
        setSEState(start,end);
        if (start>=0){
            max = Math.max(end,higher);
            min = Math.max(start,lower);
        }else if(start<=0){
            max = Math.max(start,higher);
            min = Math.max(end,lower);
        }
    }

    /**
     * 设置收开盘的同时将状态设置好
     * @param start 开盘价
     * @param end 收盘价
     */
    private void setSEState(float start,float end) {
        this.start = start;
        this.end = end;
        if (start>end){
            this.state = DROP;
        }else if (start<end){
            this.state = RAISE;
        }else{
            this.state = LEVEL;
        }
    }

    public float start() {
        return start;
    }

    public float end() {
        return end;
    }

    public float higher() {
        return higher;
    }

    public float lower() {
        return lower;
    }

    public String date() {
        return date;
    }

    public int state() {
        return state;
    }

    public float maxValue() {
        return max;
    }

    public float minValue() {
        return min;
    }

    public PointF bottomPointF() {
        return bottomPointF;
    }

    public PointF raisePointF() {
        return raisePointF;
    }

    public PointF dropPointF() {
        return dropPointF;
    }
    public PointF topPointF() {
        return topPointF;
    }
}
