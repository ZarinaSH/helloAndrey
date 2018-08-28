package com.example.rssreader.business.rss_feed_widget;

import android.util.ArrayMap;

import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.widget_settings.IWidgetSettingsRepository;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

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

    @Override
    public Flow<WidgetSettings> loadWidgetInfo(final int widgetId) {
        return mWidgetSettingsRepository.getWidgetSettingsByWidgetId(widgetId);
    }
}
