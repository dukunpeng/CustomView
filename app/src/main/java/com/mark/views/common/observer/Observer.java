package com.mark.views.common.observer;

/**
 * @author Mark
 * @Date on 2018/11/22
 **/
public interface Observer<T> {

    void update(T t, int type);

}
