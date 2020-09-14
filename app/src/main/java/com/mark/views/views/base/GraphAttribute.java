package com.mark.views.views.base;

/**
 * @Author : Mark
 * Date    : 2020/7/7  15:42
 * Desc    :曲线图属性配置 */
public class GraphAttribute extends BaseAttribute{


    public GraphAttribute(boolean isNeedGrid,boolean isHorizonGridTrue,boolean isVerticalGridTrue,boolean isNeedOutRect, boolean isNeedTouch, boolean isNeedScroll, boolean isBézier, boolean isNeedScale, boolean isNeedGradient,int gridPaintColor, int[] paddings, int[] margins, int[] gridMatrix, int yCalibrationCount) {
        this.isNeedGrid = isNeedGrid;
        this.isHorizonGridTrue = isHorizonGridTrue;
        this.isVerticalGridTrue = isVerticalGridTrue;
        this.isNeedOutRect = isNeedOutRect;
        this.isNeedTouch = isNeedTouch;
        this.isNeedScroll = isNeedScroll;
        this.isBézier = isBézier;
        this.isNeedScale = isNeedScale;
        this.isNeedGradient = isNeedGradient;
        this.gridPaintColor = gridPaintColor;
        this.paddings = paddings;
        this.margins = margins;
        this.gridMatrix = gridMatrix;
        this.yCalibrationCount = yCalibrationCount;
    }

    /**
     * 是否需要线性渐变
     */
    public boolean isNeedGradient;





    public static class GraphAttributeBuilder extends BaseAttribute {

        /**
         * 是否需要线性渐变
         */
        public boolean isNeedGradient;


        public GraphAttributeBuilder setNeedGrid(boolean needGrid) {
            isNeedGrid = needGrid;
            return this;
        }

        public GraphAttributeBuilder setHorizonGridTrue(boolean horizonGridTrue) {
            isHorizonGridTrue = horizonGridTrue;
            return this;
        }

        public GraphAttributeBuilder setVerticalGridTrue(boolean verticalGridTrue) {
            isVerticalGridTrue = verticalGridTrue;
            return this;
        }

        public GraphAttributeBuilder setNeedOutRect(boolean needOutRect) {
            isNeedOutRect = needOutRect;
            return this;
        }

        public GraphAttributeBuilder setNeedTouch(boolean needTouch) {
            isNeedTouch = needTouch;
            return this;
        }

        public GraphAttributeBuilder setNeedScroll(boolean needScroll) {
            isNeedScroll = needScroll;
            return this;
        }

        public GraphAttributeBuilder setNeedScale(boolean needScale) {
            isNeedScale = needScale;
            return this;
        }

        public GraphAttributeBuilder setPaddings(int[] paddings) {
            this.paddings = paddings;
            return this;
        }

        public GraphAttributeBuilder setMargins(int[] margins) {
            this.margins = margins;
            return this;
        }

        public GraphAttributeBuilder setGridMatrix(int[] gridMatrix) {
            this.gridMatrix = gridMatrix;
            return this;
        }

        public GraphAttributeBuilder setGridPaintColor(int gridPaintColor) {
            this.gridPaintColor = gridPaintColor;
            return this;
        }

        public GraphAttributeBuilder setyCalibrationCount(int yCalibrationCount) {
            this.yCalibrationCount = yCalibrationCount;
            return this;
        }

        public GraphAttributeBuilder setNeedGradient(boolean needGradient) {
            isNeedGradient = needGradient;
            return this;
        }
        public GraphAttributeBuilder setGrid(boolean isNeedGrid,boolean isHorizonGridTrue,boolean isVerticalGridTrue){
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
        public GraphAttributeBuilder setPermissions(boolean needOutRect,boolean needTouch,boolean needScroll,boolean isNeedScale){
            this.isNeedOutRect = needOutRect;
            this.isNeedTouch = needTouch;
            this.isNeedScroll = needScroll;
            this.isNeedScale = isNeedScale;
            return this;
        }


        /**
         *
         * @param paddings
         * @param margins
         * @param gridMatrix 网格数量
         * @return
         */
        public GraphAttributeBuilder setArrays(int[] paddings,int[] margins,int[] gridMatrix){
            this.paddings = paddings;
            this.margins = margins;
            this.gridMatrix = gridMatrix;
            return this;
        }
        public GraphAttributeBuilder setBézier(boolean bézier) {
            isBézier = bézier;
            return this;
        }

        public GraphAttribute build() {
            return new GraphAttribute( isNeedGrid, isHorizonGridTrue, isVerticalGridTrue, isNeedOutRect,  isNeedTouch,  isNeedScroll,  isBézier,  isNeedScale,  isNeedGradient,gridPaintColor,  paddings,  margins,  gridMatrix,  yCalibrationCount);
        }


    }

}
