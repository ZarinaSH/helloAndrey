package com.example.rssreader.data.widget_settings;

import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.core.Flow;

import java.net.URL;

public interface IWidgetSettingsRepository {

    Flow<Boolean> saveRssUrl(WidgetSettings widgetSettings);

    Flow<Boolean> loadAndSaveData(URL url, int widgetId);

    Flow<WidgetSettings> getWidgetSettingsByWidgetId(int widgetId);

}
