package com.example.rssreader.di.app;

import com.example.rssreader.di.widget_settings.IWidgetSettingsComponent;
import com.example.rssreader.di.widget_settings.WidgetModule;

public interface IAppComponent {

    IWidgetSettingsComponent plus(WidgetModule widgetModule);

}
