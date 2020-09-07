package com.mark.views.views.base;

/**
 * @Author : Mark
 * Date    : 2020/7/7  15:42
 * Desc    :
 */
public class GraphAttribute {
    public boolean isNeedOutRect;
    /**
     * 是否为贝塞尔曲线
     */
    public boolean isBézier;
    /**
     * 是否需要滑动
     */
    public boolean isNeedScroll;
    /**
     * 是否需要缩放
     */
    public boolean isNeedScale;
    /**
     * 触摸的时候是否需要竖线
     */
    public boolean isNeedVerticalLine;

    /**
     * 是否需要线性渐变
     */
    public boolean isNeedGradient;

    public GraphAttribute(boolean isNeedOutRect, boolean isBézier, boolean isNeedScroll, boolean isNeedScale, boolean isNeedVerticalLine, boolean isNeedGradient) {
        this.isNeedOutRect = isNeedOutRect;
        this.isBézier = isBézier;
        this.isNeedScroll = isNeedScroll;
        this.isNeedScale = isNeedScale;
        this.isNeedVerticalLine = isNeedVerticalLine;
        this.isNeedGradient = isNeedGradient;
    }

    public static class GraphAttributeBuilder {
        public boolean isNeedOutRect;

        /**
         * 是否为贝塞尔曲线
         */
        public boolean isBézier = true;

        /**
         * 是否需要滑动
         */
        public boolean isNeedScroll = true;
        /**
         * 是否需要缩放
         */
        public boolean isNeedScale;

        /**
         * 触摸的时候是否需要竖线
         */
        public boolean isNeedVerticalLine = true;

        /**
         * 是否需要线性渐变
         */
        public boolean isNeedGradient;

        public GraphAttributeBuilder setNeedGradient(boolean needGradient) {
            isNeedGradient = needGradient;
            return this;
        }

        public GraphAttributeBuilder setNeedOutRect(boolean needOutRect) {
            isNeedOutRect = needOutRect;
            return this;
        }

        public GraphAttributeBuilder setBézier(boolean bézier) {
            isBézier = bézier;
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

        public GraphAttributeBuilder setNeedVerticalLine(boolean needVerticalLine) {
            isNeedVerticalLine = needVerticalLine;
            return this;
        }

        protected GraphAttribute build() {
            return new GraphAttribute(isNeedOutRect, isBézier, isNeedScroll, isNeedScale, isNeedVerticalLine, isNeedGradient);
        }


    }

}
