package com.example.rssreader.utils;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static android.appwidget.AppWidgetManager.*;

public class RssFeedUpdateRequestReceiver extends BroadcastReceiver {

    public static final String ACTION_START_UPDATE = "ACTION_UPDATE_EXERCISE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RssFeedUpdate", "onReceive: ");
        if (intent.getAction() != null) {
            int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            if (widgetId != INVALID_APPWIDGET_ID) {
                switch (intent.getAction()) {
                    case ACTION_START_UPDATE: {
                        scheduleJob(context, widgetId);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unknown action.");
                    }
                }
            }
        }
    }

    private void scheduleJob(final Context context, final int widgetId) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getSimpleName());
        wakeLock.acquire();
        RssFeedUpdateService.startActionUpdateRssFeed(context, widgetId);
        wakeLock.release();
    }

}
