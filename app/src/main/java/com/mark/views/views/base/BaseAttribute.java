package com.mark.views.views.base;

/**
 * @Author : Mark
 * Date    : 2020/9/10  10:44
 * Desc    :图表的公共属性，体现在
 */
public class BaseAttribute {

    /**
     * 是否需要网格
     */
    public boolean isNeedGrid;
    /**
     * 网格横向是否实线
     */
    public boolean isHorizonGridTrue;
    /**
     * 网格竖向是否实线
     */
    public boolean isVerticalGridTrue;
    /**
     * 是否需要画外框
     */
    public boolean isNeedOutRect;

    /**
     * 是否需要触摸事件
     */
    public boolean isNeedTouch;
    /**
     * 是否需要滑动事件
     */
    public boolean isNeedScroll;

    /**
     * 是否为贝塞尔曲线
     */
    public boolean isBézier;

    /**
     * 是否需要缩放
     */
    public boolean isNeedScale;

    /**外框距
     * 四周边边距距单位dp
     */
    public int[] paddings = {10,10,10,10};

    /**
     * 图形距图形框距离 单位dp
     */
    public int[] margins = {10,10,10,10};


    /**
     * x,y轴的网格刻度数
     */
    public int[] gridMatrix = new int[]{18, 6};

    public int gridPaintColor ;


    /**
     * y轴的刻度数量
     */
    public int yCalibrationCount = 6;

}
