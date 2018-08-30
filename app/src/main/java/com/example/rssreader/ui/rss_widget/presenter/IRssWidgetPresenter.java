package com.example.rssreader.ui.rss_widget.presenter;

import com.example.rssreader.ui.rss_widget.view.IRssWidgetView;
import com.example.rssreader.utils.IDefaultPresenter;

public interface IRssWidgetPresenter extends IDefaultPresenter<IRssWidgetView>{

    void loadRssFeed(int widgetId);

    void updateRssFeeds(final int widgetId);

    void nextFeedClick(int widgetId);

    void prevFeedClick(int widgetId);

    void loadWidgetInfo(int widgetId);

    void onDeleted(int[] appWidgetIds);
}
