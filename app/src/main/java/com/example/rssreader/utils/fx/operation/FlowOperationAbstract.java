package com.example.rssreader.utils.fx.operation;

import java.util.concurrent.Semaphore;

public abstract class FlowOperationAbstract<T, R> {

    private final Semaphore mSemaphore;

    public abstract R call(T t);

    public FlowOperationAbstract(Semaphore semaphore) {
        mSemaphore = semaphore;
    }

    R run(T t) {
        try {
            mSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        R call = call(t);
        mSemaphore.release();
        return call;
    }

}
