package com.example.rssreader;

import android.app.Application;

import com.example.rssreader.business.widget_settings.WidgetSettingsInteractor;
import com.example.rssreader.data.rss_feed.RssFeedParser;
import com.example.rssreader.data.rss_feed.RssFeedRepository;
import com.example.rssreader.data.sqlite.RssReaderDbHelper;
import com.example.rssreader.data.sqlite.sql_provider.SQLiteDbProvider;
import com.example.rssreader.data.widget_settings.WidgetSettingsRepository;
import com.example.rssreader.ui.settings.presenter.WidgetSettingsPresenter;
import com.example.rssreader.utils.di.DI;
import com.example.rssreader.utils.internet_checker.NetworkAvailableChecker;
import com.example.rssreader.utils.url_validator.SimpleUrlValidator;

public class RssReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RssReaderDbHelper.init(getApplicationContext());
        DI.getInstance()
                .addDependency(new WidgetSettingsPresenter(
                        new WidgetSettingsInteractor(
                                new WidgetSettingsRepository(
                                        new SQLiteDbProvider(),
                                        new RssFeedRepository(
                                                new RssFeedParser(), new SQLiteDbProvider())),
                                new SimpleUrlValidator(), new NetworkAvailableChecker(getApplicationContext()))));
    }


}
