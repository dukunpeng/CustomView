package com.mark.views.views.base;

import java.util.List;

/**
 * @Author : Mark
 * Date    : 2020/7/7  13:55
 * Desc    :
 */
public interface ILine {

    /**
     * 数据点
     *
     * @return
     */
    List<IPoint> points();

    /**
     * 线条颜色
     *
     * @return
     */
    int color();

    /**
     * 16进制色值字符串
     *
     * @return
     */
    String colorHexString();

    /**
     * 线条名称
     *
     * @return
     */
    String name();

    /**
     * 是否描绘每个点
     *
     * @return
     */
    boolean isShowPoints();

    /**
     * 唯一标识id
     *
     * @return
     */
    int id();
}
