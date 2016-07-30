package me.shiyan.mvptest;

/**
 * Created by shiyan on 2016/7/26.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
