package com.example.rssreader.business.rss_feed_widget;

import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.core.Flow;
import com.example.rssreader.utils.optional.Optional;

import java.util.List;

public interface IRssFeedWidgetInteractor {

    Flow<Boolean> loadRssFeedByWidgetId(int widgetId);

    Flow<WidgetSettings> loadWidgetInfo(int widgetId);

    Flow<List<RssFeed>> loadUpdatedFeeds(final int widgetId);

    Optional<RssFeed> getNextFeed(int widgetId);

    Optional<RssFeed> getPrevFeed(int widgetId);

    Optional<RssFeed> getFirstFeed(int widgetId);
}
