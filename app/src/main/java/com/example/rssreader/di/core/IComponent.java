package com.example.rssreader.di.core;

public interface IComponent<T, R> {
    R inject(T t);
}
