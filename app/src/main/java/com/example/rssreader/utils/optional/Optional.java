package com.example.rssreader.utils.optional;

import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

import java.util.concurrent.Callable;

public class Optional<T> {

    private T val;

    private Optional(T t) {
        val = t;
    }

    public static <T> Optional<T> of(T val) {
        return new Optional<>(val);
    }

    public T getOrElse(T defaultVal) {
        return val == null ? val : defaultVal;
    }

    public Optional<T> actIfNotAbsent(Action action) {
        if (!isPresent())
            action.invoke();
        return this;
    }

    public Optional<T> actIfAbsent(Action1<T> tAction1){
        if (isPresent()){
            tAction1.invoke(val);
        }
        return this;
    }

    public <R> Optional<R> map(Func<? super T, ? extends R> mapper){
        return new Optional<>(mapper.call(val));
    }

    public T get(){
        return val;
    }

    public boolean isPresent(){
        return val != null;
    }

    public void actValOrDefault(Action1<T> tAction1, T defaultVal) {
        if (val == null)
            tAction1.invoke(defaultVal);
    }

    public Flow<T> asFlow(){
        return Flow.fromCallable(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return val;
            }
        });
    }

}
