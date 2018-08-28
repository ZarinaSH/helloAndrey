package com.example.rssreader.data.sqlite.settings;

import android.provider.BaseColumns;

import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.*;

public class RssSettingsContract {

    public static class WidgetSettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "widget_settings";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_WIDGET_ID = "widget_id";
    }

    public static String getCreateTableQuery() {
        return new StringBuilder().append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append(" (")
                .append(COLUMN_NAME_WIDGET_ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
                .append(COLUMN_NAME_URL)
                .append(" TEXT) ").toString();
    }

    public static String getDeleteTableQuery() {
        return new StringBuilder()
                .append("DROP TABLE IF EXISTS ")
                .append(TABLE_NAME).toString();
    }

}
