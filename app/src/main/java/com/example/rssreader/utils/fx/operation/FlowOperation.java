package com.example.rssreader.utils.fx.operation;

public interface FlowOperation<T, R> {
    R invoke(T t) throws Exception;
}
