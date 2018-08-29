package com.example.rssreader.data.rss_feed;

import android.util.Xml;

import com.example.rssreader.entity.RssFeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RssFeedParser implements IRssFeedParser {

    private Set<Integer> mGuidSet = new HashSet<>();

    public List<RssFeed> parse(final InputStream inputStream, final int widgetId) {
        String title = "";
        String description = "";
        String guid = "";
        int guidHash = -1;
        boolean isItem = false;
        List<RssFeed> rssFeedds = new ArrayList<>();

        try (InputStream is = inputStream) {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(is, null);
            xmlPullParser.nextTag();

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();
                String tagName = xmlPullParser.getName();

                if (tagName == null) {
                    continue;
                }

                if (eventType == XmlPullParser.END_TAG) {
                    if (tagName.equalsIgnoreCase("item"))
                        continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String val = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    val = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (tagName.equalsIgnoreCase("title")) {
                    title = trimString(val);
                } else if (tagName.equalsIgnoreCase("description")) {
                    description = trimString(val);
                } else if (tagName.equalsIgnoreCase("guid")) {
                    guid = trimString(val);
                    guidHash = guid.hashCode();
                }

                if (!title.isEmpty() && !description.isEmpty() && !guid.isEmpty()) {
                    if (isItem && !mGuidSet.contains(guidHash)) {
                        mGuidSet.add(guidHash);
                        long timestamp = System.currentTimeMillis();
                        RssFeed rssFeed = new RssFeed(title, description, guid, guidHash, timestamp, widgetId);
                        rssFeedds.add(rssFeed);
                    }

                    guid = "";
                    title = "";
                    description = "";
                    guidHash = -1;
                    isItem = false;
                }


            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return rssFeedds;
    }

    private String trimString(String s) {
        return s.trim();
    }

}
