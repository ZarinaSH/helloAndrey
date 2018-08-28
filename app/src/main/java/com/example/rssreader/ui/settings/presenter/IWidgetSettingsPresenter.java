package com.example.rssreader.ui.settings.presenter;

import com.example.rssreader.ui.settings.view.IWidgetSettingsView;
import com.example.rssreader.utils.IDefaultPresenter;

public interface IWidgetSettingsPresenter extends IDefaultPresenter<IWidgetSettingsView> {

    void saveRssUrl(String rssUrl, int widgetId);

    void loadDataFromServer(int widgetId, String rssUrl);

}
