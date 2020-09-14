package com.mark.views.views.base;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Mark
 * Date    : 2020/9/8  16:19
 * Desc    :
 */
public class Line implements ILine {

    private String colorHexString;
    private String name;
    private boolean isShowPoints;
    private int id;
    private int color;
    private List<IPoint> points;

    public Line(int color, String name, boolean isShowPoints, int id, List<IPoint> points) {
        this.colorHexString = colorHexString;
        this.name = name;
        this.isShowPoints = isShowPoints;
        this.id = id;
        this.points = points;
        this.color = color;
        this.colorHexString = intToHexValue(color) ;
    }

    @Override
    public List<IPoint> points() {
        if (points==null){
            points = new ArrayList<>();
        }
        return points;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String colorHexString() {
        return colorHexString;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isShowPoints() {
        return isShowPoints;
    }

    @Override
    public int id() {
        return id;
    }
    public Line addPoint(IPoint... points){
        for (IPoint point :points ) {
            points().add(point);
        }
        return this;
    }

    public static String intToHexValue(int number) {
        String result = Integer.toHexString(number & 0xff);
        while (result.length() < 2) {
            result = "0" + result;
        }
        return String.format("#%s",result.toUpperCase());
    }
}
