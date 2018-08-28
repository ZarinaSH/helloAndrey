package com.example.rssreader.business.widget_settings;

import com.example.rssreader.data.widget_settings.IWidgetSettingsRepository;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.utils.fx.core.Flow;
import com.example.rssreader.utils.internet_checker.INetworkAvailableChecker;
import com.example.rssreader.utils.url_validator.IUrlValidator;

import java.net.MalformedURLException;
import java.net.URL;

public class WidgetSettingsInteractor implements IWidgetSettingsInteractor {

    private final IWidgetSettingsRepository mWidgetSettingsRepository;
    private final IUrlValidator mUrlValidator;
    private final INetworkAvailableChecker mNetworkAvailableChecker;

    public WidgetSettingsInteractor(IWidgetSettingsRepository widgetSettingsRepository, IUrlValidator urlValidator,
                                    INetworkAvailableChecker networkAvailableChecker) {
        mWidgetSettingsRepository = widgetSettingsRepository;
        mUrlValidator = urlValidator;
        mNetworkAvailableChecker = networkAvailableChecker;
    }

    @Override
    public boolean urlIsValid(String url) {
        return mUrlValidator.validate(url);
    }

    @Override
    public Flow<Boolean> saveRssUrlForWidget(String rssUrl, int widgetId) {
        return mWidgetSettingsRepository.saveRssUrl(new WidgetSettings(rssUrl, widgetId));
    }

    @Override
    public Flow<Boolean> loadAndSaveData(String rssUrl, int widgetId) {
        try {
            URL rssURL = new URL(rssUrl);
            return mWidgetSettingsRepository.loadAndSaveData(rssURL, widgetId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Flow.error(new IllegalArgumentException());
    }

    @Override
    public Flow<Boolean> checkNetworkAvailable() {
        return mNetworkAvailableChecker.checkNetworkAvailable();
    }

}
