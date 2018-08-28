package com.example.rssreader.utils;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.rss_feed.RssFeedParser;
import com.example.rssreader.data.rss_feed.RssFeedRepository;
import com.example.rssreader.data.sqlite.sql_provider.SQLiteDbProvider;
import com.example.rssreader.data.widget_settings.IWidgetSettingsRepository;
import com.example.rssreader.data.widget_settings.WidgetSettingsRepository;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.ui.rss_widget.view.RssReaderProvider;
import com.example.rssreader.ui.settings.view.ConfigureActivity;
import com.example.rssreader.utils.fx.operation.Subscriber;

import java.net.URL;
import java.util.List;

import static android.appwidget.AppWidgetManager.*;
import static com.example.rssreader.ui.rss_widget.view.RssReaderProvider.INIT_ACTION;
import static com.example.rssreader.ui.rss_widget.view.RssReaderProvider.UPDATE_DATA;

public class RssFeedUpdateService extends IntentService {

    private static final String ACTION_RSS_FEED_UPDATE = "com.example.rssreader.action.ACTION_RSS_FEED_UPDATE";
    private IRssFeedStorage mRssFeedStorage;
    private IWidgetSettingsRepository mWidgetSettingsRepository;

    public static void startActionUpdateRssFeed(final Context context, final int widgetId) {
        Intent intent = new Intent(context, RssFeedUpdateService.class);
        intent.setAction(ACTION_RSS_FEED_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_ID, widgetId);
        context.startService(intent);
    }

    public RssFeedUpdateService() {
        super(RssFeedUpdateService.class.getSimpleName());
        SQLiteDbProvider sqLiteDbProvider = new SQLiteDbProvider();
        mRssFeedStorage = new RssFeedRepository(new RssFeedParser(), sqLiteDbProvider);
        mWidgetSettingsRepository = new WidgetSettingsRepository(sqLiteDbProvider, mRssFeedStorage);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equalsIgnoreCase(ACTION_RSS_FEED_UPDATE)) {
                int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
                if (widgetId != INVALID_APPWIDGET_ID) {
                    Log.d("RssFeedUpdateService", "onHandleIntent: " + System.currentTimeMillis());
                    mWidgetSettingsRepository.getWidgetSettingsByWidgetId(widgetId)
                            .subscribe(new Subscriber<WidgetSettings>() {
                                @Override
                                public void onData(WidgetSettings data) {
                                    updateRssFeeds(data);
                                }

                                @Override
                                public void onError(Throwable throwable) {

                                }
                            });
                }
            }
        }
    }

    private void updateRssFeeds(final WidgetSettings widgetSettings) {
        try {
            mRssFeedStorage.updateAndSaveRssFeed(new URL(widgetSettings.getUrl()), widgetSettings.getWidgetId())
                    .subscribe(new Subscriber<List<RssFeed>>() {
                        @Override
                        public void onData(List<RssFeed> data) {
                            Log.d("RssFeedUpdateService", "onData: " + data.size());
                            Intent intent = new Intent(RssFeedUpdateService.this, RssReaderProvider.class);
                            intent.setAction(UPDATE_DATA);
                            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetSettings.getWidgetId());
                            sendBroadcast(intent);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
