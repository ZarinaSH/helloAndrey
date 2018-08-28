package com.example.rssreader.data.rss_feed;

import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.utils.fx.core.Flow;

import java.net.URL;
import java.util.List;

public interface IRssFeedStorage {

    Flow<List<RssFeed>> loadRssFeed(URL url, int widgetId);

    boolean saveData(List<RssFeed> rssFeeds);

    Flow<List<RssFeed>> getRssFeedsByWidgetId(final int widgetId);

    Flow<List<RssFeed>> updateAndSaveRssFeed(final URL url, final int widgetId);

}
