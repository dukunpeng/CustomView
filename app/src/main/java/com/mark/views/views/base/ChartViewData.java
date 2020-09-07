package com.mark.views.views.base;


import android.util.Log;

import com.mark.views.common.observer.AbstractObservable;

/**
 * @Author : Mark
 * Date    : 2020/3/22  16:55
 * Desc    :被观察数据源
 */
public class ChartViewData extends AbstractObservable<IRootChartData> {

    private final String TAG = getClass().getSimpleName();

    private IRootChartData chartData;

    public ChartViewData initData(IRootChartData chartData) {
        this.chartData = chartData;
        return this;
    }

    /**
     * 全量替换
     *
     * @param chartData
     * @return
     */
    public ChartViewData replaceData(IRootChartData chartData) {
        this.chartData = chartData;
        return this;
    }

    /**
     * 添加点数据，此处添加的顺序需和lines的顺序一致
     * 如果有空点情况，可设置state {@link IPoint#state()}进行标记
     *
     * @param points
     * @return
     */
    public ChartViewData addPoints(IPoint... points) {
        if (points == null) {
            Log.e(TAG, "points is null");
            return this;
        }
        if (points.length != chartData.lines().size()) {
            Log.e(TAG, "points length is not match lines size");
            return this;
        }
        for (int i = 0; i < chartData.lines().size(); i++) {
            chartData.lines().get(i).points().add(points[i]);
        }
        return this;
    }

    /**
     * 添加线条
     *
     * @param line
     * @return
     */
    public ChartViewData addLine(ILine line) {
        if (this.chartData != null) {
            chartData.lines().add(line);
        }
        return this;
    }

    /**
     * 替换线条
     *
     * @param line
     * @return
     */
    public ChartViewData replaceLine(ILine line) {
        for (int i = 0; i < chartData.lines().size(); i++) {
            if (chartData.lines().get(i).id() == line.id()) {
                chartData.lines().remove(i);
                chartData.lines().add(line);
            }
        }
        return this;
    }


    /**
     * 全量更新
     */
    public void update() {
        update(0);
    }

    public void update(int type) {
        notify(chartData, type);
    }
}
