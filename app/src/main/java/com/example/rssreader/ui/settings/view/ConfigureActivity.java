package com.example.rssreader.ui.settings.view;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.example.rssreader.R;
import com.example.rssreader.ui.rss_widget.view.RssReaderProvider;
import com.example.rssreader.ui.settings.presenter.IWidgetSettingsPresenter;
import com.example.rssreader.ui.settings.presenter.WidgetSettingsPresenter;
import com.example.rssreader.utils.di.DI;

import static android.appwidget.AppWidgetManager.*;
import static android.widget.Toast.*;
import static com.example.rssreader.ui.rss_widget.view.RssReaderProvider.INIT_ACTION;

public class ConfigureActivity extends AppCompatActivity implements IWidgetSettingsView {

    private AppWidgetManager mAppWidgetManager;
    private IWidgetSettingsPresenter mSettingsPresenter;
    private EditText mRssUrlEditText;
    private View mBannerView;
    int appWidgetId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);
        mSettingsPresenter = DI.getInstance().inject(IWidgetSettingsPresenter.class);

        mSettingsPresenter.bindView(this);
        mRssUrlEditText = findViewById(R.id.input_rss_url);
        mBannerView = findViewById(R.id.loadingBanner);
        initToolbar();
        mAppWidgetManager = getInstance(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        if (extras != null && extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID) != INVALID_APPWIDGET_ID) {
            appWidgetId = extras.getInt(EXTRA_APPWIDGET_ID);
        } else {
            finish();
            return;
        }

        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.rss_reader);
        mAppWidgetManager.updateAppWidget(appWidgetId, views);


        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsPresenter.saveRssUrl(mRssUrlEditText.getText().toString(), appWidgetId);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_title);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void showInvalidUrlMessage(@StringRes int invalidUrlMsg) {
        showToast(invalidUrlMsg);
    }

    @Override
    public void onSuccessUrlSave(@StringRes int successUrlSaveMsg) {
        showToast(successUrlSaveMsg);
        mSettingsPresenter.loadDataFromServer(appWidgetId, mRssUrlEditText.getText().toString());
    }

    @Override
    public void showLoadBanner() {
        mBannerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBannerView() {
        mBannerView.setVisibility(View.GONE);
    }

    public void onSuccessLoadData() {
        Intent intent = new Intent(ConfigureActivity.this, RssReaderProvider.class);
        intent.setAction(INIT_ACTION);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), RssReaderProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(intent);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void showToast(@StringRes int resId) {
        makeText(this, resId, LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkNotAvailableToast(@StringRes int resId) {
        showToast(resId);
    }
}
