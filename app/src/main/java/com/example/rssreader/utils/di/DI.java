package com.example.rssreader.utils.di;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DI {

    private Map<Type, Object> dependency = new HashMap<>();

    private static DI instance = new DI();

    private DI(){

    }

    public static DI getInstance() {
        return instance;
    }

    public void addDependency(Object impl){
        dependency.put(impl.getClass().getGenericInterfaces()[0], impl);
        System.out.println();
    }

    public <T> T inject(Class aClass){
        return (T) dependency.get(((Type)aClass));
    }

}
