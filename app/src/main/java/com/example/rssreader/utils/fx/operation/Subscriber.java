package com.example.rssreader.utils.fx.operation;

public interface Subscriber<T> {

    void onData(T data);

    void onError(Throwable throwable);

}
