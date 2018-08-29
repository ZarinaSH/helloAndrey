package com.example.rssreader.business.rss_feed_widget;

import android.util.ArrayMap;
import android.util.Log;

import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.widget_settings.IWidgetSettingsRepository;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class RssFeedWidgetInteractor implements IRssFeedWidgetInteractor {

    private final IRssFeedStorage mRssFeedRepository;
    private final IWidgetSettingsRepository mWidgetSettingsRepository;
    private final ArrayMap<Integer, List<RssFeed>> mRssFeedsCache;
    private final ArrayMap<Integer, Integer> mWidgetFeedsCursor = new ArrayMap<>();
    private final static String TAG = "RssFeedWidgetInteractor";

    public RssFeedWidgetInteractor(final IRssFeedStorage rssFeedRepository,
                                   final IWidgetSettingsRepository widgetSettingsRepository) {
        mRssFeedRepository = rssFeedRepository;
        mWidgetSettingsRepository = widgetSettingsRepository;
        mRssFeedsCache = new ArrayMap<>();
    }

    @Override
    public Flow<Boolean> loadRssFeedByWidgetId(final int widgetId) {
        mWidgetFeedsCursor.put(widgetId, 0);
        if (mRssFeedsCache.get(widgetId) == null || mRssFeedsCache.get(widgetId).size() == 0) {
            return mRssFeedRepository.getRssFeedsByWidgetId(widgetId)
                    .map(new Func<List<RssFeed>, Boolean>() {
                        @Override
                        public Boolean call(List<RssFeed> rssFeeds) {
                            mRssFeedsCache.put(widgetId, rssFeeds);
                            return true;
                        }
                    });
        } else {
            return Flow.fromCallable(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return mRssFeedsCache.get(widgetId) != null && mRssFeedsCache.get(widgetId).size() != 0;
                }
            });
        }
    }

    public Flow<List<RssFeed>> loadUpdatedFeeds(final int widgetId) {
        List<RssFeed> rssFeeds = mRssFeedsCache.get(widgetId);
        RssFeed max = Collections.max(rssFeeds, new Comparator<RssFeed>() {
            @Override
            public int compare(RssFeed o1, RssFeed o2) {
                return Long.compare(o1.getSavedTimestamp(), o2.getSavedTimestamp());
            }
        });
        return mRssFeedRepository.getRssFeedsLaterTime(widgetId, max.getSavedTimestamp())
                .map(new Func<List<RssFeed>, List<RssFeed>>() {
                    @Override
                    public List<RssFeed> call(List<RssFeed> rssFeeds) {
                        mRssFeedsCache.put(widgetId, rssFeeds);
                        mWidgetFeedsCursor.put(widgetId, 0);
                        Log.d(TAG, "call: " + rssFeeds.size());
                        return rssFeeds;
                    }
                });
    }

    @Override
    public Flow<WidgetSettings> loadWidgetInfo(final int widgetId) {
        return mWidgetSettingsRepository.getWidgetSettingsByWidgetId(widgetId);
    }

    @Override
    public RssFeed getNextFeed(int widgetId) {
        Integer oldCursorVal = mWidgetFeedsCursor.get(widgetId);
        List<RssFeed> rssFeeds = mRssFeedsCache.get(widgetId);
        if (rssFeeds.size() == oldCursorVal) {
            return rssFeeds.get(oldCursorVal);
        }
        mWidgetFeedsCursor.put(widgetId, ++oldCursorVal);
        return rssFeeds.get(oldCursorVal);
    }

    @Override
    public RssFeed getPrevFeed(int widgetId) {
        Integer oldCursorVal = mWidgetFeedsCursor.get(widgetId);
        List<RssFeed> rssFeeds = mRssFeedsCache.get(widgetId);
        if (oldCursorVal == 0) {
            return rssFeeds.get(oldCursorVal);
        }
        mWidgetFeedsCursor.put(widgetId, --oldCursorVal);
        return rssFeeds.get(oldCursorVal);
    }

    public RssFeed getFirstFeed(int widgetId) {
        return mRssFeedsCache.get(widgetId).get(0);
    }

    private List<RssFeed> concatRssFeedsLists(List<RssFeed> oldList, List<RssFeed> newList) {
        List<RssFeed> result = new ArrayList<>(oldList);
        result.addAll(0, newList);
        return result;
    }

}
