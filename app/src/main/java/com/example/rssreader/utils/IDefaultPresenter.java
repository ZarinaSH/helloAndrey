package com.example.rssreader.utils;

public interface IDefaultPresenter<T> {

    void bindView(T view);

    void unbindView();
}
