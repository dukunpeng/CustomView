package com.mark.views.views.base;

import android.graphics.PointF;

/**
 * @Author : Mark
 * Date    : 2020/7/7  13:55
 * Desc    :每个点的数据
 * 因为接口成员变量默认修饰符为 public static final
 * 此接口使用时，每个接口方法里面不能调用其它方法，只能将数据算好以后，直接赋值给接口方法返回。
 */
public interface IPoint {

    /**
     * 原始描述
     *
     * @return
     */
    String originalKey();

    /**
     * 体现在图上的描述
     *
     * @return
     */
    String key();

    /**
     * Y轴对外描述
     *
     * @return
     */
    String valueYString();

    /**
     * 数值
     *
     * @return
     */
    float valueY();

    /**
     * 用于扩展的状态，默认值为0
     *
     * @return
     */
    int state();

    /**
     * 实际绘制的点
     *
     * @return
     */
    PointF pointF();

}
