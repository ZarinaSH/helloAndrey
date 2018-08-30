package com.example.rssreader.ui.rss_widget.view;

import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;

public interface IRssWidgetView {

    void showProgressBar(int widgetId);

    void hideProgressBar(int widgetId);

    void showData(RssFeed rssFeed);

    void showErrorToast(String message);

    void showWidgetInfo(WidgetSettings data);

    void stopAlarm(int widgetId);
}
