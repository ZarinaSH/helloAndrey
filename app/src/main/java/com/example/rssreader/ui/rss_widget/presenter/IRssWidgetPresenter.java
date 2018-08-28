package com.example.rssreader.ui.rss_widget.presenter;

import com.example.rssreader.ui.rss_widget.view.IRssWidgetView;
import com.example.rssreader.utils.IDefaultPresenter;

public interface IRssWidgetPresenter extends IDefaultPresenter<IRssWidgetView>{

    void loadRssFeed(int widgetId);

    void nextFeedClick();

    void prevFeedClick();

    void loadWidgetInfo(int widgetId);
}
