package com.example.rssreader.di.widget_settings;

import com.example.rssreader.di.core.DI;
import com.example.rssreader.ui.settings.presenter.IWidgetSettingsPresenter;
import com.example.rssreader.ui.settings.view.IWidgetSettingsView;

public class WidgetSettingsComponent implements IWidgetSettingsComponent {

    @Override
    public IWidgetSettingsPresenter inject(IWidgetSettingsView iWidgetSettingsView) {
        return DI.getInstance().getDependency(iWidgetSettingsView);
    }
}
