package com.example.rssreader.business.widget_settings;

import com.example.rssreader.utils.fx.core.Flow;

public interface IWidgetSettingsInteractor {

    boolean urlIsValid(String url);

    Flow<Boolean> saveRssUrlForWidget(String rssUrl, int widgetId);

    Flow<Boolean> loadAndSaveData(String rssUrl, int widgetId);

    Flow<Boolean> checkNetworkAvailable();

}
