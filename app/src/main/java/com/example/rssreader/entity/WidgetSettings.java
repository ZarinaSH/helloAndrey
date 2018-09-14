package com.example.rssreader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.rssreader.data.sqlite.settings.RssSettingsContract;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataField;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataObject;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataTable;
import com.example.rssreader.utils.sql_helpers.create_table.entity.Type;

import static com.example.rssreader.data.sqlite.settings.RssSettingsContract.WidgetSettingsEntry.COLUMN_NAME_WIDGET_ID;
import static com.example.rssreader.utils.sql_helpers.create_table.entity.Type.INTEGER;

@DataTable(tableName = "widget_settings")
public class WidgetSettings implements Parcelable, DataObject{

    @DataField(type = Type.TEXT)
    private final String url;
    @DataField(type = INTEGER, isPrimaryKey = true, name = COLUMN_NAME_WIDGET_ID)
    private final int widgetId;

    public WidgetSettings(String url, int widgetId) {
        this.url = url;
        this.widgetId = widgetId;
    }

    public String getUrl() {
        return url;
    }

    public int getWidgetId() {
        return widgetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WidgetSettings that = (WidgetSettings) o;

        if (widgetId != that.widgetId) return false;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + widgetId;
        return result;
    }

    protected WidgetSettings(Parcel in) {
        url = in.readString();
        widgetId = in.readInt();
    }

    public static final Creator<WidgetSettings> CREATOR = new Creator<WidgetSettings>() {
        @Override
        public WidgetSettings createFromParcel(Parcel in) {
            return new WidgetSettings(in);
        }

        @Override
        public WidgetSettings[] newArray(int size) {
            return new WidgetSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(widgetId);
    }
}
