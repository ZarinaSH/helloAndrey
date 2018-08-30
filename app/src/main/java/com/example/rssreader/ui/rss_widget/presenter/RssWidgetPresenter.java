package com.example.rssreader.ui.rss_widget.presenter;

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
import com.example.rssreader.utils.optional.Action;
import com.example.rssreader.utils.optional.Action1;

import java.util.List;

public class RssWidgetPresenter implements IRssWidgetPresenter {

    private IRssWidgetView mView;
    private final IRssFeedWidgetInteractor mRssFeedWidgetInteractor;

    public RssWidgetPresenter() {
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
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onData(Boolean data) {
                        mView.hideProgressBar(widgetId);
                        mRssFeedWidgetInteractor.getFirstFeed(widgetId)
                                .actIfAbsent(new Action1<RssFeed>() {
                                    @Override
                                    public void invoke(RssFeed rssFeed) {
                                        mView.showData(rssFeed);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showErrorToast(throwable.getMessage());
                        mView.hideProgressBar(widgetId);
                    }
                });
    }

    @Override
    public void updateRssFeeds(final int widgetId) {
        mView.showProgressBar(widgetId);
        mRssFeedWidgetInteractor.loadUpdatedFeeds(widgetId)
                .subscribe(new Subscriber<List<RssFeed>>() {
                    @Override
                    public void onData(List<RssFeed> data) {
                        mView.hideProgressBar(widgetId);
                        mRssFeedWidgetInteractor
                                .getFirstFeed(widgetId)
                                .actIfAbsent(new Action1<RssFeed>() {
                                    @Override
                                    public void invoke(RssFeed rssFeed) {
                                        mView.showData(rssFeed);
                                    }
                                });

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showErrorToast(throwable.getMessage());
                        mView.hideProgressBar(widgetId);
                    }
                });
    }

    @Override
    public void nextFeedClick(final int widgetId) {
        mRssFeedWidgetInteractor.getNextFeed(widgetId)
                .actIfAbsent(new Action1<RssFeed>() {
                    @Override
                    public void invoke(RssFeed rssFeed) {
                        mView.showData(rssFeed);
                    }
                });

    }

    @Override
    public void prevFeedClick(final int widgetId) {
        mRssFeedWidgetInteractor.getPrevFeed(widgetId)
                .actIfAbsent(new Action1<RssFeed>() {
                    @Override
                    public void invoke(RssFeed rssFeed) {
                        mView.showData(rssFeed);
                    }
                });
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
