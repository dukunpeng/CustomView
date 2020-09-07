package com.mark.views.views.base;

/**
 * @Author : Mark
 * Date    : 2020/9/7  9:48
 * Desc    :K线图天数据model
 */
public class KPointModel {

    /**
     * 开盘价
     */
    private double start;
    /**
     * 收盘价
     */
    private double end;

    /**
     * 最高点
     */
    private double higher;

    /**
     * 最低点
     */
    private double lower;

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
    private double max;
    /**
     * 四个数据最小值
     */
    private double min;

    public KPointModel(double start, double end, double higher, double lower, String date) {
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
    private void setSEState(double start,double end) {
        this.start = start;
        this.end = end;
        if (start>end){
            this.state = -1;
        }else if (start<end){
            this.state = 1;
        }else{
            this.state = 0;
        }
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double getHigher() {
        return higher;
    }

    public double getLower() {
        return lower;
    }

    public String getDate() {
        return date;
    }

    public int getState() {
        return state;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}
