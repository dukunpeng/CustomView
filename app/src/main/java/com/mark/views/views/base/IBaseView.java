package com.mark.views.views.base;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @Author : Mark
 * Date    : 2020/7/6  13:35
 * Desc    :
 */
public interface IBaseView {

    /**
     * 初始化view参数
     *此时不能做变量赋值
     * @param context
     * @param attr
     */
    void initView(Context context, AttributeSet attr);

    /**
     * 初始化变量
     */
    void initParams();

    /**
     * 初始化所有画笔
     */
    void initPaint();

    /**
     * 计算数据
     *
     * @return
     */
    BaseView calculateRootData();

    /**
     * 过滤数据源
     *
     * @return
     */
    BaseView filter();

}
