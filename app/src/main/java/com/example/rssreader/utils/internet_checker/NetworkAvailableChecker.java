package com.example.rssreader.utils.internet_checker;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.rssreader.utils.fx.core.Flow;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class NetworkAvailableChecker implements INetworkAvailableChecker {

    private final WeakReference<Context> mContextRef;

    public NetworkAvailableChecker(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Override
    public Flow<Boolean> checkNetworkAvailable() {
        return Flow.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (isNetworkAvailable(mContextRef.get())) {
                    HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://google.com").openConnection());
                    urlConnection.setRequestProperty("User-Agent", "Test");
                    urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    return urlConnection.getResponseCode() == 200;
                }
                return false;
            }
        });
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

}
