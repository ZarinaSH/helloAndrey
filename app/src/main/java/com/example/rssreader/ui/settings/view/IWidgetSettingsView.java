package com.example.rssreader.ui.settings.view;

import android.support.annotation.StringRes;

public interface IWidgetSettingsView {
    void showInvalidUrlMessage(@StringRes int invalidUrlMsg);

    void onSuccessUrlSave(@StringRes int successUrlSaveMsg);

    void showLoadBanner();

    void hideBannerView();

    void onSuccessLoadData();

    void showNetworkNotAvailableToast(@StringRes int resId);
}
