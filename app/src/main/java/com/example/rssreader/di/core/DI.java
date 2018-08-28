package com.example.rssreader.di.core;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DI {

    private static DI mInstance;
    private Map<Type, Object> mDependenciesMap = new HashMap<>();
    private WeakReference<Context> mContextRef;

    private DI(){

    }

    public static DI getInstance(){
        if (mInstance == null){
            mInstance = new DI();
        }
        return mInstance;
    }

    public void register(Object obj){
        mDependenciesMap.put(obj.getClass(), obj);
    }

    public void register(Type type, Object obj){
        mDependenciesMap.put(type, obj);
    }

    @SuppressWarnings("unchecked")
    public <R> R getDependency(Object obj){
        return (R) mDependenciesMap.get(obj.getClass().getGenericInterfaces()[0]);
    }

    public void setContext(Context context){
        mContextRef = new WeakReference<>(context);
    }

    public Context getContext() {
        return mContextRef.get();
    }
}
