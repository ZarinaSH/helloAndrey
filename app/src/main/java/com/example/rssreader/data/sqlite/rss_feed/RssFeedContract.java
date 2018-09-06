package com.example.rssreader.data.sqlite.rss_feed;

import android.provider.BaseColumns;

import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.utils.sql_helpers.create_table.parser.TableParser;

import static com.example.rssreader.data.sqlite.rss_feed.RssFeedContract.RssFeedEntry.TABLE_NAME;

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
        public static final String COLUMN_NAME_IS_VISIBLE = "is_visible";
        public static final String COLUMN_NAME_WIDGET_ID = "widget_id";
    }

    public static String getCreateTableQuery() {
        return TableParser.generateCreateQuery(RssFeed.class);
    }

    public static String getDeleteTableQuery() {
        return new StringBuilder()
                .append("DROP TABLE IF EXISTS ")
                .append(TABLE_NAME).toString();
    }

}
