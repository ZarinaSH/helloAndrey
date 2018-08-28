package com.example.rssreader.di.app;

import com.example.rssreader.di.widget_settings.IWidgetSettingsComponent;
import com.example.rssreader.di.widget_settings.WidgetModule;
import com.example.rssreader.di.widget_settings.WidgetSettingsComponent;

public class AppComponent implements IAppComponent {

    private final AppModule mAppModule;

    public AppComponent(AppModule appModule){
        mAppModule = appModule;
    }

    @Override
    public IWidgetSettingsComponent plus(WidgetModule widgetModule) {
        return new WidgetSettingsComponent();
    }
}
