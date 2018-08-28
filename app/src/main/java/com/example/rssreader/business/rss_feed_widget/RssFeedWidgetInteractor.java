package com.example.rssreader.business.rss_feed_widget;

import android.util.ArrayMap;
import android.util.Log;

import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.widget_settings.IWidgetSettingsRepository;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class RssFeedWidgetInteractor implements IRssFeedWidgetInteractor {

    private final IRssFeedStorage mRssFeedRepository;
    private final IWidgetSettingsRepository mWidgetSettingsRepository;
    private final ArrayMap<Integer, List<RssFeed>> mRssFeedsCache;

    public RssFeedWidgetInteractor(final IRssFeedStorage rssFeedRepository,
                                   final IWidgetSettingsRepository widgetSettingsRepository) {
        mRssFeedRepository = rssFeedRepository;
        mWidgetSettingsRepository = widgetSettingsRepository;
        mRssFeedsCache = new ArrayMap<>();
    }

    @Override
    public Flow<List<RssFeed>> loadRssFeedByWidgetId(final int widgetId) {
        if (mRssFeedsCache.get(widgetId) == null || mRssFeedsCache.get(widgetId).size() == 0) {
            return mRssFeedRepository.getRssFeedsByWidgetId(widgetId)
                    .map(new Func<List<RssFeed>, List<RssFeed>>() {
                        @Override
                        public List<RssFeed> call(List<RssFeed> rssFeeds) {
                            mRssFeedsCache.put(widgetId, rssFeeds);
                            return rssFeeds;
                        }
                    });
        } else {
            return Flow.fromCallable(new Callable<List<RssFeed>>() {
                @Override
                public List<RssFeed> call() {
                    return mRssFeedsCache.get(widgetId);
                }
            });
        }
    }

    public Flow<List<RssFeed>> loadUpdatedFeeds(final int widgetId) {
        List<RssFeed> rssFeeds = mRssFeedsCache.get(widgetId);
        RssFeed max = Collections.max(rssFeeds, new Comparator<RssFeed>() {
            @Override
            public int compare(RssFeed o1, RssFeed o2) {
                return Integer.compare(o1.getSavedTimestamp(), o2.getSavedTimestamp());
            }
        });
        return mRssFeedRepository.getRssFeedsLaterTime(widgetId, max.getSavedTimestamp())
                .map(new Func<List<RssFeed>, List<RssFeed>>() {
                    @Override
                    public List<RssFeed> call(List<RssFeed> rssFeeds) {
                        Log.d("RssFeedWidgetInteractor", "call: " + rssFeeds.size());
                        return rssFeeds;
                    }
                });
    }

    @Override
    public Flow<WidgetSettings> loadWidgetInfo(final int widgetId) {
        return mWidgetSettingsRepository.getWidgetSettingsByWidgetId(widgetId);
    }
}
