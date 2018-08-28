package com.example.rssreader.data.rss_feed;

import com.example.rssreader.entity.RssFeed;

import java.io.InputStream;
import java.util.List;

public interface IRssFeedParser {

    List<RssFeed> parse(InputStream inputStream, int widgetId);

}
