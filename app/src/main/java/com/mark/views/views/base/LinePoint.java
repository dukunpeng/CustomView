package com.mark.views.views.base;

import android.graphics.PointF;

/**
 * @Author : Mark
 * Date    : 2020/9/8  17:12
 * Desc    :
 */
public class LinePoint implements IPoint {

    private String key;
    private String valueYString;
    private float valueY;
    private int state;
    private PointF pointF;

    public LinePoint(String key, String valueYString, float valueY, int state, PointF pointF) {
        this.key = key;
        this.valueYString = valueYString;
        this.valueY = valueY;
        this.state = state;
        this.pointF = pointF;
    }

    @Override
    public String originalKey() {
        return key;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String valueYString() {
        return valueYString;
    }

    @Override
    public float valueY() {
        return valueY;
    }

    @Override
    public int state() {
        return state;
    }

    @Override
    public PointF pointF() {
        return pointF;
    }
}
