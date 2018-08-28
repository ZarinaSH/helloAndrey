package com.example.rssreader.ui.rss_widget.presenter;

import android.util.Log;

import com.example.rssreader.business.rss_feed_widget.IRssFeedWidgetInteractor;
import com.example.rssreader.business.rss_feed_widget.RssFeedWidgetInteractor;
import com.example.rssreader.data.rss_feed.IRssFeedStorage;
import com.example.rssreader.data.rss_feed.RssFeedRepository;
import com.example.rssreader.data.sqlite.sql_provider.SQLiteDbProvider;
import com.example.rssreader.data.widget_settings.WidgetSettingsRepository;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.ui.rss_widget.view.IRssWidgetView;
import com.example.rssreader.utils.fx.operation.Subscriber;

import java.util.List;

public class RssWidgetPresenter implements IRssWidgetPresenter {

    private IRssWidgetView mView;
    private final IRssFeedWidgetInteractor mRssFeedWidgetInteractor;
    private volatile List<RssFeed> mData;
    private int cursor;
    private final String TAG = "RssWidgetPresenter";


    public RssWidgetPresenter() {
        cursor = 0;
        SQLiteDbProvider sqLiteDbProvider = new SQLiteDbProvider();
        IRssFeedStorage rssFeedRepository = new RssFeedRepository(null, sqLiteDbProvider);
        mRssFeedWidgetInteractor = new RssFeedWidgetInteractor(
                rssFeedRepository
                , new WidgetSettingsRepository(sqLiteDbProvider, rssFeedRepository)
        );
    }

    @Override
    public void loadRssFeed(final int widgetId) {
        mView.showProgressBar(widgetId);
        mRssFeedWidgetInteractor.loadRssFeedByWidgetId(widgetId)
                .subscribe(new Subscriber<List<RssFeed>>() {
                    @Override
                    public void onData(List<RssFeed> data) {
                        mData = data;
                        mView.hideProgressBar(widgetId);
                        mView.showData(data.get(0));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showErrorToast(throwable.getMessage());
                        mView.hideProgressBar(widgetId);
                    }
                });
    }

    @Override
    public void updateRssFeeds(final int widgetId){
        Log.d(TAG, "updateRssFeeds: ");
        mView.showProgressBar(widgetId);
        mRssFeedWidgetInteractor.loadUpdatedFeeds(widgetId)
                .subscribe(new Subscriber<List<RssFeed>>() {
                    @Override
                    public void onData(List<RssFeed> data) {
                        mData = data;
                        cursor = 0;
                        mView.hideProgressBar(widgetId);
                        mView.showData(data.get(0));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showErrorToast(throwable.getMessage());
                        mView.hideProgressBar(widgetId);
                    }
                });
        Log.d(TAG, "updateRssFeeds: end");
    }

    @Override
    public void nextFeedClick() {
        try {
            cursor++;
            mView.showData(mData.get(cursor));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void prevFeedClick() {
        try {
            cursor--;
            mView.showData(mData.get(cursor));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadWidgetInfo(int widgetId) {
        mRssFeedWidgetInteractor.loadWidgetInfo(widgetId)
                .subscribe(new Subscriber<WidgetSettings>() {
                    @Override
                    public void onData(WidgetSettings data) {
                        mView.showWidgetInfo(data);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }

    @Override
    public void bindView(IRssWidgetView view) {
        if (mView == null) {
            mView = view;
        } else if (mView.hashCode() != view.hashCode()) {
            mView = view;
        }
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
