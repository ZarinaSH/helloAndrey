package com.example.rssreader.ui.settings.presenter;

import android.appwidget.AppWidgetManager;
import android.content.Intent;

import com.example.rssreader.R;
import com.example.rssreader.business.widget_settings.IWidgetSettingsInteractor;
import com.example.rssreader.ui.rss_widget.view.RssReaderProvider;
import com.example.rssreader.ui.settings.view.ConfigureActivity;
import com.example.rssreader.ui.settings.view.IWidgetSettingsView;
import com.example.rssreader.utils.fx.operation.Subscriber;

import static com.example.rssreader.ui.rss_widget.view.RssReaderProvider.INIT_ACTION;

public class WidgetSettingsPresenter implements IWidgetSettingsPresenter {

    private IWidgetSettingsView mView;
    private final IWidgetSettingsInteractor mSettingsInteractor;

    public WidgetSettingsPresenter(final IWidgetSettingsInteractor widgetSettingsInteractor) {
        mSettingsInteractor = widgetSettingsInteractor;
    }

    @Override
    public void saveRssUrl(final String rssUrl, final int widgetId) {
        mView.showLoadBanner();
        mSettingsInteractor.checkNetworkAvailable()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onData(Boolean data) {
                        if (data) {
                            startRssUrlSave(rssUrl, widgetId);
                        } else {
                            showNetworkNotAvailableMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showNetworkNotAvailableMessage();
                    }
                });
    }

    private void startRssUrlSave(final String rssUrl, final int widgetId) {
        if (mSettingsInteractor.urlIsValid(rssUrl)) {
            mSettingsInteractor.saveRssUrlForWidget(rssUrl, widgetId)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onData(Boolean data) {
                            mView.onSuccessUrlSave(R.string.success_url_save_msg);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mView.showInvalidUrlMessage(R.string.invalid_url);
                            mView.hideBannerView();
                        }
                    });
        } else {
            mView.showInvalidUrlMessage(R.string.invalid_url);
        }
    }

    @Override
    public void loadDataFromServer(final int widgetId, final String rssUrl) {
        mSettingsInteractor.loadAndSaveData(rssUrl, widgetId)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onData(Boolean data) {
                        mView.hideBannerView();
                        mView.sendInitWidgetBroadcast(getInitWidgetIntent(widgetId));
                        mView.onSuccessLoadData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.hideBannerView();
                    }
                });
    }

    private Intent getInitWidgetIntent(int widgetId){
        Intent intent = new Intent();
        intent.setAction(INIT_ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        return intent;
    }

    @Override
    public void bindView(IWidgetSettingsView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    private void showNetworkNotAvailableMessage() {
        mView.showNetworkNotAvailableToast(R.string.network_not_available);
        mView.hideBannerView();
    }

}
