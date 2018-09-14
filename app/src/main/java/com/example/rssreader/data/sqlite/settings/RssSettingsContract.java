package com.example.rssreader.data.sqlite.settings;

import android.provider.BaseColumns;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.sql_helpers.create_table.parser.TableParser;
import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.*;

public class RssSettingsContract {

    public static class WidgetSettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "widget_settings";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_WIDGET_ID = "widget_id";
    }

    public static String getCreateTableQuery() {
        return TableParser.generateCreateQuery(WidgetSettings.class);
    }


    public static String getDeleteTableQuery() {
        return new StringBuilder()
                .append("DROP TABLE IF EXISTS ")
                .append(TABLE_NAME).toString();
    }

}
