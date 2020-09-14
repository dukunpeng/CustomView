package com.mark.views.views.base;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import java.util.HashSet;

/**
 * @Author : Mark
 * Date    : 2020/9/14  10:07
 * Desc    :
 */
public class CustomScrollView extends ScrollView {
    private int lastX;
    private int lastY;
    private int moveX;
    private int moveY;
    private int mTouchSlop;

    private HashSet<View> views = new HashSet<>();

    public CustomScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void addDisallowViews(View... views){
        for (View view : views){
            this.views.add(view);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
      int c =  e.getPointerCount();
      if (c==2){
          for (View view : views) {
           view.getParent().requestDisallowInterceptTouchEvent(true);
          }
      }else{
          for (View view : views) {
              view.getParent().requestDisallowInterceptTouchEvent(false);
          }
      }
        /*switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) e.getRawY();
                moveX = (int) e.getRawX();
                if (Math.abs(moveX - downX) > mTouchSlop) {
                    return false;
                }
        }*/
        return super.onInterceptTouchEvent(e);
    }

  /*  @Override
    public boolean onTouchEvent(MotionEvent e) {
         switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveY = (int) e.getY();
                moveX = (int) e.getX();
                Log.i("CustomScrollView","---"+(lastY-moveY)+"------"+(lastX-moveX)+"---"+mTouchSlop);
                if (lastX!=0&&(Math.abs(lastY-moveY)-Math.abs(lastX-moveX)<0)&&Math.abs(moveX - lastX) > mTouchSlop) {
                    return false;
                }
                lastX = moveX;
                lastY = moveY;
                break;
        }
        return super.onTouchEvent(e);
    }*/
}
