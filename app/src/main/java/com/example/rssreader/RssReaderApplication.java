package com.example.rssreader;

import android.app.Application;
import com.example.rssreader.data.sqlite.RssReaderDbHelper;
import com.example.rssreader.di.app.AppComponent;
import com.example.rssreader.di.app.AppModule;
import com.example.rssreader.di.app.IAppComponent;

public class RssReaderApplication extends Application {

    private static IAppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        RssReaderDbHelper.init(getApplicationContext());
        appComponent = new AppComponent(new AppModule(getApplicationContext()));
    }

    public static IAppComponent getAppComponent(){
        return appComponent;
    }

}
