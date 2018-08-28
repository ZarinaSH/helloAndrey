package com.example.rssreader.business.rss_feed_widget;

import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.core.Flow;

import java.util.List;

public interface IRssFeedWidgetInteractor {

    Flow<List<RssFeed>> loadRssFeedByWidgetId(int widgetId);

    Flow<WidgetSettings> loadWidgetInfo(int widgetId);

    Flow<List<RssFeed>> loadUpdatedFeeds(final int widgetId);
}
