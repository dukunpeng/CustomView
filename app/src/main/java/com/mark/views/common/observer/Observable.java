package com.mark.views.common.observer;

/**
 * @author Mark
 * @Date on 2018/11/22
 **/
public interface Observable<T> {
    void register(Observer observer);

    void unRegister(Observer observer);

    void notify(T t, int type);
}
