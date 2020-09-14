package com.mark.views;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @Author : Mark
 * Date    : 2020/9/14  16:59
 * Desc    :
 */
public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean mCanVerticalScroll = true;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        if (!mCanVerticalScroll){
            return false;
        }else {
            return super.canScrollVertically();
        }
    }

    public void setCanVerticalScroll(boolean b){
        mCanVerticalScroll = b;
    }
}