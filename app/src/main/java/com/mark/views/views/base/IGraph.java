package com.mark.views.views.base;

import android.graphics.Canvas;

/**
 * @Author : Mark
 * Date    : 2020/9/10  11:19
 * Desc    :基础图形接口
 */
public interface IGraph {

    /**
     * 创建属性
     * @return
     */
    BaseAttribute buildAttribute();

    void drawOutRect(Canvas canvas);
    void drawGridLine(Canvas canvas);
    void drawLines(Canvas canvas);
    void drawTouch(Canvas canvas);

    void calculateByUpdate();
    void calculateAll();
    /**
     * 检测是否匹配上
     *
     * @param key
     * @param mid
     * @return -1 代表key 小 1 代表key大  0代表key合适
     */
     int check(float key, int mid);

    /**
     * 根据X轴坐标算下标
     * @param x
     * @return
     */
    int getIndexByX(float x);
    /**
     * 是否需要网格
     * @return
     */
     boolean isNeedGrid();
    /**
     * 网格横向是否实线
     */
     boolean isHorizonGridTrue();
    /**
     * 网格竖向是否实线
     */
     boolean isVerticalGridTrue();
    /**
     * 是否需要画外框
     */
     boolean isNeedOutRect();

    /**
     * 是否需要触摸事件
     */
     boolean isNeedTouch();
    /**
     * 是否需要滑动事件
     */
     boolean isNeedScroll();

    /**
     * 是否为贝塞尔曲线
     */
     boolean isBézier();


}
