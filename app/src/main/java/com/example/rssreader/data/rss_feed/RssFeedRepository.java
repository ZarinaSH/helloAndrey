package com.example.rssreader.data.rss_feed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rssreader.data.sqlite.sql_provider.IDbProvider;
import com.example.rssreader.data.sqlite.RssReaderDbHelper;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.utils.fx.Func;
import com.example.rssreader.utils.fx.core.Flow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_DESCRIPTION;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_GUID;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_GUID_HASH;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_TITLE;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_WIDGET_ID;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_TIMESTAMP;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.TABLE_NAME;

public class RssFeedRepository implements IRssFeedStorage {

    private final IRssFeedParser mRssFeedParser;
    private final RssReaderDbHelper mDbProvider;

    public RssFeedRepository(IRssFeedParser rssFeedParser, IDbProvider dbProvider) {
        mRssFeedParser = rssFeedParser;
        mDbProvider = dbProvider.provide();
    }

    public Flow<List<RssFeed>> loadRssFeed(final URL rssUrl, final int widgetId) {
        return Flow.fromCallable(new Callable<List<RssFeed>>() {
            @Override
            public List<RssFeed> call() throws Exception {
                List<RssFeed> result = new ArrayList<>();
                try (InputStream inputStream = rssUrl.openConnection().getInputStream()) {
                    result = mRssFeedParser.parse(inputStream, widgetId);
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    public Flow<List<RssFeed>> updateAndSaveRssFeed(final URL url, final int widgetId) {
        return Flow.fromCallable(new Callable<List<RssFeed>>() {
            @Override
            public List<RssFeed> call() throws Exception {
                List<RssFeed> result = new ArrayList<>();
                try (InputStream inputStream = url.openConnection().getInputStream()) {
                    result = mRssFeedParser.parse(inputStream, widgetId);
                    System.out.println();
                }
                return result;
            }
        }).map(new Func<List<RssFeed>, List<RssFeed>>() {
            @Override
            public List<RssFeed> call(List<RssFeed> rssFeeds) {
                updateData(rssFeeds, widgetId);
                return rssFeeds;
            }
        });
    }

    private void updateData(final List<RssFeed> rssFeeds, final int widgetId) {
        SQLiteDatabase writableDatabase = mDbProvider.getWritableDatabase();
        for (int i = 0; i < rssFeeds.size(); i++) {
            RssFeed feed = rssFeeds.get(i);
            ContentValues contentValues = convertRssFeedToContentValues(feed);
            long id = writableDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, CONFLICT_IGNORE);
            if (id == -1) {
                writableDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_GUID_HASH + "=?", new String[]{String.valueOf(feed.getGuidHash())});
            }
        }

    }

    public boolean saveData(List<RssFeed> rssFeeds) {
        SQLiteDatabase writableDatabase = mDbProvider.getWritableDatabase();
        for (RssFeed rssFeed : rssFeeds) {
            writableDatabase.insert(TABLE_NAME, null, convertRssFeedToContentValues(rssFeed));
        }
        return true;
    }

    public Flow<List<RssFeed>> getRssFeedsByWidgetId(final int widgetId) {
        return Flow.fromCallable(new Callable<List<RssFeed>>() {
            @Override
            public List<RssFeed> call() throws Exception {
                SQLiteDatabase readableDatabase = mDbProvider.getReadableDatabase();
                String[] projection = {
                        COLUMN_NAME_TITLE,
                        COLUMN_NAME_DESCRIPTION,
                        COLUMN_NAME_TIMESTAMP,
                        COLUMN_NAME_GUID,
                        COLUMN_NAME_GUID_HASH
                };
                String selection = COLUMN_NAME_WIDGET_ID + " = ?";
                String[] selectionArgs = {Integer.toString(widgetId)};
                String sortOrder = COLUMN_NAME_TIMESTAMP + " ASC";
                Cursor cursor = readableDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                List<RssFeed> rssFeeds = mapCursorToRssFeedList(cursor, widgetId);
                Log.d("RssFeedRepository", "call: " + rssFeeds.size());
                cursor.close();
                return rssFeeds;
            }
        });
    }

    private ContentValues convertRssFeedToContentValues(RssFeed rssFeed) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(COLUMN_NAME_TITLE, rssFeed.getTitle());
        contentValues.put(COLUMN_NAME_DESCRIPTION, rssFeed.getDescription());
        contentValues.put(COLUMN_NAME_GUID, rssFeed.getGuid());
        contentValues.put(COLUMN_NAME_GUID_HASH, rssFeed.getGuidHash());
        contentValues.put(COLUMN_NAME_TIMESTAMP, rssFeed.getSavedTimestamp());
        contentValues.put(COLUMN_NAME_WIDGET_ID, rssFeed.getWidgetId());
        return contentValues;
    }


    private List<RssFeed> mapCursorToRssFeedList(final Cursor cursor, final int widgetId) {
        List<RssFeed> rssFeeds = new ArrayList<>();
        while (cursor.moveToNext()) {
            rssFeeds.add(mapCursorToRssFeed(cursor, widgetId));
        }
        return rssFeeds;
    }

    private RssFeed mapCursorToRssFeed(final Cursor cursor, final int widgetId) {
        String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DESCRIPTION));
        String guid = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GUID));
        int guidHashCode = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_GUID_HASH));
        int timestamp = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TIMESTAMP));
        return new RssFeed(title, description, guid, guidHashCode, timestamp, widgetId);
    }

}
