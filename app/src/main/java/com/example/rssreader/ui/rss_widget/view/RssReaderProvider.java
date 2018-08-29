package com.example.rssreader.ui.rss_widget.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.rssreader.R;
import com.example.rssreader.entity.RssFeed;
import com.example.rssreader.entity.WidgetBtn;
import com.example.rssreader.entity.WidgetSettings;
import com.example.rssreader.ui.rss_widget.presenter.IRssWidgetPresenter;
import com.example.rssreader.ui.rss_widget.presenter.RssWidgetPresenter;
import com.example.rssreader.ui.settings.view.ConfigureActivity;
import com.example.rssreader.utils.RssFeedUpdateRequestReceiver;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import static android.app.PendingIntent.*;
import static android.appwidget.AppWidgetManager.*;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.rssreader.entity.WidgetBtn.NEXT;
import static com.example.rssreader.entity.WidgetBtn.PREV;


public class RssReaderProvider extends AppWidgetProvider implements IRssWidgetView {

    private static final String TAG = "RssReaderProvider";
    private static IRssWidgetPresenter mRssWidgetPresenter;
    private final int mAlarmRepeatPeriod = 60 * 1000;
    private WeakReference<Context> mContextReference = null;
    public final static String INIT_ACTION = "init";
    public final static String UPDATE_DATA = "update_data";

    void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                         final int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_reader);
        //region settings btn
        Intent intent = new Intent(context, ConfigureActivity.class);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.widget_config_btn, pendingIntent);
        //endregion
        views.setOnClickPendingIntent(R.id.nextBtn, getBtnClickIntent(context, "updateUp", appWidgetId, NEXT));
        views.setOnClickPendingIntent(R.id.prevBtn, getBtnClickIntent(context, "updateDown", appWidgetId, PREV));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: " + hashCode());
        mContextReference = new WeakReference<>(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        mContextReference = new WeakReference<>(context);
        mRssWidgetPresenter = new RssWidgetPresenter();
        mRssWidgetPresenter.bindView(this);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        mContextReference = new WeakReference<>(context);
        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(INIT_ACTION)) {
                int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
                if (widgetId != INVALID_APPWIDGET_ID) {
                    mRssWidgetPresenter.loadWidgetInfo(widgetId);
                    mRssWidgetPresenter.loadRssFeed(widgetId);
                    startAlarm(context, widgetId);
                }
                return;
            }
            if (intent.getAction().equalsIgnoreCase(UPDATE_DATA)) {
                int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
                if (widgetId != INVALID_APPWIDGET_ID) {
                    mRssWidgetPresenter.loadWidgetInfo(widgetId);
                    mRssWidgetPresenter.updateRssFeeds(widgetId);
                }
                return;
            }
        }
        try {
            int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            if (widgetId != INVALID_APPWIDGET_ID && mRssWidgetPresenter != null) {
                WidgetBtn widgetBtn = (WidgetBtn) intent.getSerializableExtra("widget_btn");
                if (widgetBtn.equals(NEXT)) {
                    mRssWidgetPresenter.nextFeedClick(widgetId);
                } else {
                    mRssWidgetPresenter.prevFeedClick(widgetId);
                }
            }
        } catch (Exception e) {
            showErrorToast(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startAlarm(final Context context, final int widgetId) {
        Intent updateIntent = new Intent(context, RssFeedUpdateRequestReceiver.class);
        updateIntent.setAction(RssFeedUpdateRequestReceiver.ACTION_START_UPDATE);
        updateIntent.putExtra(EXTRA_APPWIDGET_ID, widgetId);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getBroadcast(context, 0, updateIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mAlarmRepeatPeriod, mAlarmRepeatPeriod, pendingIntent);
        context.sendBroadcast(updateIntent);
    }

    @Override
    public void showData(RssFeed rssFeed) {
        Context context = mContextReference.get();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_reader);
        views.setTextViewText(R.id.widget_title, rssFeed.getTitle());
        views.setTextViewText(R.id.widget_description, rssFeed.getDescription());
        setOnClickBtns(views, context, rssFeed.getWidgetId());
        getInstance(context).updateAppWidget(rssFeed.getWidgetId(), views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        mRssWidgetPresenter.unbindView();
        mRssWidgetPresenter = null;
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        mContextReference = new WeakReference<>(context);
    }

    private void setOnClickBtns(final RemoteViews views, final Context context, int widgetId) {
        views.setOnClickPendingIntent(R.id.nextBtn, getBtnClickIntent(context, "updateUp", widgetId, NEXT));
        views.setOnClickPendingIntent(R.id.prevBtn, getBtnClickIntent(context, "updateDown", widgetId, PREV));
    }

    private PendingIntent getBtnClickIntent(final Context context, final String action, final int widgetId, final WidgetBtn widgetBtn) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("widget_btn", widgetBtn);
        intent.putExtra(EXTRA_APPWIDGET_ID, widgetId);
        return getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void showProgressBar(int widgetId) {
        Context context = mContextReference.get();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_reader);
        views.setViewVisibility(R.id.widgetProgressBanner, VISIBLE);
        setOnClickBtns(views, context, widgetId);
        getInstance(context).updateAppWidget(new ComponentName(context, RssReaderProvider.class), views);
    }

    @Override
    public void hideProgressBar(int widgetId) {
        Context context = mContextReference.get();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_reader);
        views.setViewVisibility(R.id.widgetProgressBanner, GONE);
        setOnClickBtns(views, context, widgetId);
        getInstance(context).updateAppWidget(new ComponentName(context, RssReaderProvider.class), views);
    }

    @Override
    public void showWidgetInfo(final WidgetSettings data) {
        RemoteViews views = new RemoteViews(mContextReference.get().getPackageName(), R.layout.rss_reader);

    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(mContextReference.get(), message, Toast.LENGTH_SHORT).show();
    }
}

