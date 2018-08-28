package com.example.rssreader.data.widget_settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.sqlite.sql_provider.IDbProvider;
import com.example.rssreader.data.sqlite.RssReaderDbHelper;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.COLUMN_NAME_URL;
import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.COLUMN_NAME_WIDGET_ID;
import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.TABLE_NAME;

public class WidgetSettingsRepository implements IWidgetSettingsRepository {

    private final String TAG = "WidgetSettingsRepo";

    private final RssReaderDbHelper mRssReaderDbHelper;
    private final IRssFeedStorage mRssFeedRepository;

    public WidgetSettingsRepository(IDbProvider dbProvider, IRssFeedStorage rssFeedRepository) {
        mRssReaderDbHelper = dbProvider.provide();
        mRssFeedRepository = rssFeedRepository;
    }

    @Override
    public Flow<Boolean> saveRssUrl(final WidgetSettings widgetSettings) {
        return Flow.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                SQLiteDatabase writableDatabase = mRssReaderDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                int widgetId = widgetSettings.getWidgetId();
                values.put(COLUMN_NAME_WIDGET_ID, widgetId);
                values.put(COLUMN_NAME_URL, widgetSettings.getUrl());
                int updateResult = writableDatabase.update(TABLE_NAME, values, COLUMN_NAME_WIDGET_ID.concat("=?"), new String[]{Integer.toString(widgetId)});
                if (updateResult == 0) {
                    writableDatabase.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
                return true;
            }
        });
    }

    @Override
    public Flow<Boolean> loadAndSaveData(URL url, int widgetId) {
        return mRssFeedRepository.loadRssFeed(url, widgetId)
                .map(new Func<List<RssFeed>, Boolean>() {
                    @Override
                    public Boolean call(List<RssFeed> rssFeeds) {
                        return mRssFeedRepository.saveData(rssFeeds);
                    }
                });
    }

    @Override
    public Flow<WidgetSettings> getWidgetSettingsByWidgetId(final int widgetId) {
        return Flow.fromCallable(new Callable<WidgetSettings>() {
            @Override
            public WidgetSettings call() throws Exception {
                SQLiteDatabase readableDatabase = mRssReaderDbHelper.getReadableDatabase();
                String[] projection = {
                        COLUMN_NAME_WIDGET_ID,
                        COLUMN_NAME_URL
                };

                String selection = COLUMN_NAME_WIDGET_ID + " = ?";
                String[] selectionArgs = {String.valueOf(widgetId)};
                Cursor cursor = readableDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                cursor.moveToFirst();
                WidgetSettings widgetSettings = mapCursorToWidgetSettings(cursor);
                cursor.close();
                return widgetSettings;
            }
        });
    }

    private WidgetSettings mapCursorToWidgetSettings(final Cursor cursor) {
        int widgetId = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_WIDGET_ID));
        String widgetUrl = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_URL));
        return new WidgetSettings(widgetUrl, widgetId);
    }


}
