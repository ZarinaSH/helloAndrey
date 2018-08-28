package com.example.rssreader.di.app;

import android.content.Context;

import com.example.rssreader.di.core.DI;

import java.lang.ref.WeakReference;

public class AppModule {

    private final WeakReference<Context> mContextWeakReference;

    public AppModule(final Context context){
        mContextWeakReference = new WeakReference<>(context);
        DI.getInstance().setContext(context);
    }

    public Context provideContext(){
        return mContextWeakReference.get();
    }

}
