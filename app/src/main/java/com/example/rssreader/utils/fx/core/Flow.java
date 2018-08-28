package com.example.rssreader.utils.fx.core;

import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.operation.OnDataSubscriber;
import com.example.rssreader.utils.fx.operation.Subscriber;

import java.util.concurrent.Callable;

public class Flow<T> {

    private FlowExecutor flowExecutor;

    private Flow(Callable<? extends T> callable) {
        flowExecutor = new FlowExecutor();
        flowExecutor.addCallable(callable);
    }

    private Flow(FlowExecutor flowExecutor){
        this.flowExecutor = flowExecutor;
    }

    public <R> Flow<R> map(Func<? super T, ? extends R> func) {
        flowExecutor.addOperation(func);
        return new Flow<>(flowExecutor);
    }

    public Flow<T> setSubscribeThread(SubscribeThread subscribeThread) {
        flowExecutor.setSubscribeThread(subscribeThread);
        return this;
    }

    public void subscribe(final Subscriber<T> subscriber) {
        flowExecutor.run(subscriber);
    }

    public void subscribe(final OnDataSubscriber<T> onDataSubscriber) {
        flowExecutor.run(onDataSubscriber);
    }

    public static <T> Flow<T> fromCallable(final Callable<? extends T> func) {
        return createCallableFlow(func);
    }

    private static <T> Flow<T> createCallableFlow(Callable<? extends T> func) {
        return new Flow<>(func);
    }

    public static <T> Flow<T> error(Throwable throwable){
        return null;
    }

}
