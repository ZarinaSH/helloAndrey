package com.example.rssreader.entity;

public class RssFeed {

    private final String title;
    private final String description;
    private final int mWidgetId;
    private final String guid;
    private final int guidHash;
    private final long savedTimestamp;

    public RssFeed(String title, String description, String guid, long savedTimestamp, int widgetId) {
        this.title = title;
        this.description = description;
        mWidgetId = widgetId;
        this.guid = guid;
        this.guidHash = guid.hashCode();
        this.savedTimestamp = savedTimestamp;
    }

    public RssFeed(String title, String description, String guid, int guidHash, long savedTimestamp, int widgetId) {
        this.title = title;
        this.description = description;
        mWidgetId = widgetId;
        this.guid = guid;
        this.guidHash = guidHash;
        this.savedTimestamp = savedTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getWidgetId() {
        return mWidgetId;
    }

    public String getGuid() {
        return guid;
    }

    public int getGuidHash() {
        return guidHash;
    }

    public long getSavedTimestamp() {
        return savedTimestamp;
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mWidgetId=" + mWidgetId +
                ", guid='" + guid + '\'' +
                ", guidHash=" + guidHash +
                ", savedTimestamp=" + savedTimestamp +
                '}';
    }
}
