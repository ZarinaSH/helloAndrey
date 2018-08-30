package com.example.rssreader.data.sqlite.rss_feed;

import android.provider.BaseColumns;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_GUID;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_GUID_HASH;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_TIMESTAMP;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_TITLE;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_WIDGET_ID;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.TABLE_NAME;
import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.COLUMN_NAME_DESCRIPTION;

public class RssFeedContract {

    public RssFeedContract() {
    }

    public static class RssFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "rss_feed_entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_GUID = "guid";
        public static final String COLUMN_NAME_GUID_HASH = "guid_hash";
        public static final String COLUMN_NAME_TIMESTAMP = "timestmp";
        public static final String COLUMN_NAME_WIDGET_ID = "widget_id";
    }

    public static String getCreateTableQuery() {
        return new StringBuilder().append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append(" (")
                .append(COLUMN_NAME_GUID_HASH)
                .append(" INTEGER PRIMARY KEY NOT NULL, ")
                .append(COLUMN_NAME_TITLE)
                .append(" TEXT, ")
                .append(COLUMN_NAME_DESCRIPTION)
                .append(" TEXT, ")
                .append(COLUMN_NAME_GUID)
                .append(" TEXT, ")
                .append(COLUMN_NAME_TIMESTAMP)
                .append(" INTEGER, ")
                .append(COLUMN_NAME_WIDGET_ID)
                .append(" INTEGER)")
                .toString();
    }

    public static String getDeleteTableQuery() {
        return new StringBuilder()
                .append("DROP TABLE IF EXISTS ")
                .append(TABLE_NAME).toString();
    }

}
