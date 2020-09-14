package com.mark.views.views.base;

import android.graphics.Color;

/**
 * @Author : Mark
 * Date    : 2020/9/7  14:38
 * Desc    :
 */
public class KGraphAttribute extends BaseAttribute{

    private KGraphAttribute(boolean isNeedGrid,boolean isHorizonGridTrue,boolean isVerticalGridTrue,boolean isNeedOutRect, boolean isNeedTouch, boolean isNeedScroll, boolean isBézier, boolean isNeedScale,int raiseColor, int dropColor, int levelColor, int gridPaintColor, int[] paddings, int[] margins, int[] gridMatrix, int yCalibrationCount, int screenXCalibrationCount, float barSpace, float hatchWidth) {
        this.isNeedGrid = isNeedGrid;
        this.isHorizonGridTrue = isHorizonGridTrue;
        this.isVerticalGridTrue = isVerticalGridTrue;
        this.isNeedOutRect = isNeedOutRect;
        this.isNeedTouch = isNeedTouch;
        this.isNeedScroll = isNeedScroll;
        this.isBézier = isBézier;
        this.isNeedScale = isNeedScale;
        this.raiseColor = raiseColor;
        this.dropColor = dropColor;
        this.levelColor = levelColor;
        this.gridPaintColor = gridPaintColor;
        this.paddings = paddings;
        this.margins = margins;
        this.gridMatrix = gridMatrix;
        this.yCalibrationCount = yCalibrationCount;
        this.screenXCalibrationCount = screenXCalibrationCount;
        this.barSpace = barSpace;
        this.hatchWidth = hatchWidth;
    }

    /***
     * 涨色
     */
    public int raiseColor ;
    /**
     * 跌色
     */
    public int dropColor ;

    /**
     * 平色
     */
    public int levelColor  ;


    /**
     * 一屏K柱个数
     */
    public int screenXCalibrationCount = 50;

    /**
     * 两个K柱相邻两边的间距，可以是0
     */
    public float barSpace = 2.0f;
    /**
     * 影线宽度
     */
    public float hatchWidth ;


    public static class KGraphAttributeBuilder extends BaseAttribute{
        /***
         * 涨色
         */
        private int raiseColor = Color.parseColor("#EA4949");
        /**
         * 跌色
         */
        private int dropColor = Color.parseColor("#52AF1E");

        /**
         * 平色
         */
        private int levelColor  = Color.parseColor("#EA4949");

        public int gridPaintColor = Color.parseColor("#e6e6e6");
        /**
         * 均线色值集合
         */

        /**外框距
         * 四周边边距距单位dp
         */
        public int[] paddings = {10,10,10,10};

        /**
         * 图形距图形框距离 单位dp
         */
        public int[] margins = {0,0,0,0};


        /**
         * x,y轴的网格刻度数
         */
        public int[] gridMatrix = new int[]{18, 6};

        /**
         * y轴的刻度数量
         */
        public int yCalibrationCount = 6;
        /**
         * 一屏K柱个数
         */
        public int screenXCalibrationCount = 50;

        /**
         * 两个K柱相邻两边的间距，可以是0
         */
        public float barSpace = 2.0f;

        /**
         * 影线宽度
         */
        private float hatchWidth = 1.0f;


        public KGraphAttributeBuilder setGrid(boolean isNeedGrid,boolean isHorizonGridTrue,boolean isVerticalGridTrue){
            this.isNeedGrid = isNeedGrid;
            this.isHorizonGridTrue = isHorizonGridTrue;
            this.isVerticalGridTrue = isVerticalGridTrue;
            return this;
        }
        /**
         *
         * @param needOutRect 外框
         * @param needTouch 触摸
         * @param needScroll 滑动
         * @return
         */
        public KGraphAttributeBuilder setPermissions(boolean needOutRect,boolean needTouch,boolean needScroll,boolean isNeedScale){
            this.isNeedOutRect = needOutRect;
            this.isNeedTouch = needTouch;
            this.isNeedScroll = needScroll;
            this.isNeedScale = isNeedScale;
            return this;
        }

        /**
         *
         * @param raiseColor 涨色
         * @param dropColor 跌色
         * @param levelColor 平色
         * @param gridPaintColor 网格色
         * @return
         */
        public KGraphAttributeBuilder setColors(int raiseColor,int dropColor,int levelColor,int gridPaintColor){
            this.raiseColor = raiseColor;
            this.dropColor = dropColor;
            this.levelColor = levelColor;
            this.gridPaintColor = gridPaintColor;
            return this;
        }

        /**
         *
         * @param paddings
         * @param margins
         * @param gridMatrix 网格数量
         * @return
         */
        public KGraphAttributeBuilder setArrays(int[] paddings,int[] margins,int[] gridMatrix){
            this.paddings = paddings;
            this.margins = margins;
            this.gridMatrix = gridMatrix;
            return this;
        }

        public KGraphAttributeBuilder setNumbers( int yCalibrationCount,int screenXCalibrationCount,float barSpace,float hatchWidth){
            this.yCalibrationCount = yCalibrationCount;
            this.screenXCalibrationCount = screenXCalibrationCount;
            this.barSpace = barSpace;
            this.hatchWidth = hatchWidth;
            return this;
        }

        public KGraphAttributeBuilder setNeedOutRect(boolean needOutRect) {
            isNeedOutRect = needOutRect;
            return this;
        }

        public KGraphAttributeBuilder setNeedTouch(boolean needTouch) {
            isNeedTouch = needTouch;
            return this;
        }

        public KGraphAttributeBuilder setNeedScroll(boolean needScroll) {
            isNeedScroll = needScroll;
            return this;
        }

        public KGraphAttributeBuilder setBézier(boolean bézier) {
            isBézier = bézier;
            return this;
        }

        public KGraphAttributeBuilder setRaiseColor(int raiseColor) {
            this.raiseColor = raiseColor;
            return this;
        }

        public KGraphAttributeBuilder setDropColor(int dropColor) {
            this.dropColor = dropColor;
            return this;
        }

        public KGraphAttributeBuilder setLevelColor(int levelColor) {
            this.levelColor = levelColor;
            return this;
        }

        public KGraphAttributeBuilder setGridPaintColor(int gridPaintColor) {
            this.gridPaintColor = gridPaintColor;
            return this;
        }

        public KGraphAttributeBuilder setPaddings(int[] paddings) {
            this.paddings = paddings;
            return this;
        }

        public KGraphAttributeBuilder setMargins(int[] margins) {
            this.margins = margins;
            return this;
        }

        public KGraphAttributeBuilder setGridMatrix(int[] gridMatrix) {
            this.gridMatrix = gridMatrix;
            return this;
        }

        public KGraphAttributeBuilder setyCalibrationCount(int yCalibrationCount) {
            this.yCalibrationCount = yCalibrationCount;
            return this;
        }

        public KGraphAttributeBuilder setScreenXCalibrationCount(int screenXCalibrationCount) {
            this.screenXCalibrationCount = screenXCalibrationCount;
            return this;
        }

        public KGraphAttributeBuilder setBarSpace(float barSpace) {
            this.barSpace = barSpace;
            return this;
        }

        public KGraphAttributeBuilder setHatchWidth(float hatchWidth) {
            this.hatchWidth = hatchWidth;
            return this;
        }

        public KGraphAttribute build(){
            return new KGraphAttribute(isNeedGrid,isHorizonGridTrue,isVerticalGridTrue,isNeedOutRect,isNeedTouch,isNeedScroll,isBézier,isNeedScale,raiseColor,dropColor,levelColor,gridPaintColor,paddings,margins,gridMatrix,yCalibrationCount,screenXCalibrationCount,barSpace,hatchWidth);
        }
    }
}
