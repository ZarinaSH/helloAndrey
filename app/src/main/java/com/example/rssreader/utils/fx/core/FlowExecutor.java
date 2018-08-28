package com.example.rssreader.utils.fx.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.operation.FlowOperation;
import com.example.rssreader.utils.fx.operation.OnDataSubscriber;
import com.example.rssreader.utils.fx.operation.Subscriber;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.rssreader.utils.fx.core.SubscribeThread.*;


@SuppressWarnings("unchecked")
public final class FlowExecutor {

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private Queue<FlowOperation> mFlowOperations = new LinkedList<>();
    private Callable mCallable;
    private SubscribeThread mSubscribeThread = MAIN;

    FlowExecutor() {
    }

    public void addCallable(Callable callable) {
        mCallable = callable;
    }

    public void addOperation(final Func func) {
        mFlowOperations.add(new FlowOperation() {
            @Override
            public Object invoke(Object o) {
                return func.call(o);
            }
        });
    }

    public void run(final Subscriber subscriber) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Future submit = mExecutorService.submit(mCallable);
                    try {
                        Object data = submit.get();
                        if (mFlowOperations.size() == 0) {
                            emitResult(subscriber, data);
                            mExecutorService.shutdownNow();
                            return;
                        }
                        while (mFlowOperations.size() != 0) {
                            FlowOperation poll = mFlowOperations.poll();
                            data = poll.invoke(data);
                        }
                        mExecutorService.shutdownNow();
                        emitResult(subscriber, data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        emmitError(subscriber, e);
                    }
                }
            }).start();
        } catch (Exception e) {
            emmitError(subscriber, e);
        }

    }

    public void setSubscribeThread(SubscribeThread subscribeThread) {
        mSubscribeThread = subscribeThread;
    }

    private void emitResult(final Subscriber subscriber, final Object data) {
        if (mSubscribeThread.equals(MAIN)) {
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.onData(data);
                        }
                    });
        } else {
            subscriber.onData(data);
        }
    }

    private void emmitError(final Subscriber subscriber, final Throwable throwable) {
        if (mSubscribeThread.equals(MAIN)) {
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.onError(throwable);
                        }
                    });
        } else {
            subscriber.onError(throwable);
        }
    }

    public <T> void run(OnDataSubscriber<T> onDataSubscriber) {

    }


}
