package com.example.rssreader.entity;

public final class RssFeed {

    private final String title;
    private final String description;
    private final int widgetId;
    private final String guid;
    private final int guidHash;
    private final long savedTimestamp;
    private final boolean visible;

    private RssFeed(String title, String description, String guid, int guidHash, long savedTimestamp, boolean visible, int widgetId) {
        this.title = title;
        this.description = description;
        this.widgetId = widgetId;
        this.guid = guid;
        this.guidHash = guidHash;
        this.savedTimestamp = savedTimestamp;
        this.visible = visible;
    }

    public static final class Builder {
        private String title;
        private String description;
        private int widgetId;
        private String guid;
        private int guidHash;
        private long savedTimestamp;
        private boolean visible;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setWidgetId(int widgetId) {
            this.widgetId = widgetId;
            return this;
        }

        public Builder setGuid(String guid) {
            this.guid = guid;
            return this;
        }

        public Builder setGuidHash(int guidHash) {
            this.guidHash = guidHash;
            return this;
        }

        public Builder setSavedTimestamp(long savedTimestamp) {
            this.savedTimestamp = savedTimestamp;
            return this;
        }

        public Builder setVisibility(boolean visibility) {
            this.visible = visibility;
            return this;
        }

        public RssFeed build() {
            return new RssFeed(title, description, guid, guidHash, savedTimestamp, visible, widgetId);
        }

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getWidgetId() {
        return widgetId;
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

    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssFeed feed = (RssFeed) o;

        if (widgetId != feed.widgetId) return false;
        if (guidHash != feed.guidHash) return false;
        return guid.equals(feed.guid);
    }

    @Override
    public int hashCode() {
        int result = widgetId;
        result = 31 * result + guid.hashCode();
        result = 31 * result + guidHash;
        return result;
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", widgetId=" + widgetId +
                ", guid='" + guid + '\'' +
                ", guidHash=" + guidHash +
                ", savedTimestamp=" + savedTimestamp +
                '}';
    }
}
