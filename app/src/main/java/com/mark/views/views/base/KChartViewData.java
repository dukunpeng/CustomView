package com.mark.views.views.base;

import android.util.Log;

import com.mark.views.common.observer.AbstractObservable;

import java.util.List;

/**
 * @Author : Mark
 * Date    : 2020/9/7  14:25
 * Desc    :K线被观察数据源
 */
public class KChartViewData extends AbstractObservable<IRootKChartData> {

    private final String TAG = getClass().getSimpleName();


    private IRootKChartData chartData;

    public KChartViewData initData(IRootKChartData chartData) {
        this.chartData = chartData;
        return this;
    }

    public KChartViewData addKPoint(KPointModel... pointModels){
        if (pointModels == null) {
            Log.e(TAG, "pointModels is null");
            return this;
        }
        for (KPointModel model :pointModels ) {
            chartData.kPoints().add(model);
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
