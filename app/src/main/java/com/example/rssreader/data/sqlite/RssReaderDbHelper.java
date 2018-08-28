package com.example.rssreader.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rssreader.data.sqlite.rss_feed.RssFeedContract;
import com.example.rssreader.data.sqlite.settings.RssSettingsContract;

public class RssReaderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RssReaderApplication.db";
    private static RssReaderDbHelper mDbHelperInstance;

    private RssReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RssSettingsContract.getCreateTableQuery());
        db.execSQL(RssFeedContract.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RssFeedContract.getDeleteTableQuery());
        db.execSQL(RssSettingsContract.getDeleteTableQuery());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public synchronized static void init(Context applicationContext) {
        mDbHelperInstance = new RssReaderDbHelper(applicationContext);
    }

    public synchronized static RssReaderDbHelper getDbHelperInstance() {
        if (mDbHelperInstance == null) {
            throw new IllegalStateException("Call init method!");
        }
        return mDbHelperInstance;
    }

    public synchronized static RssReaderDbHelper getDbHelperInstance(Context context) {
        if (mDbHelperInstance == null) {
            mDbHelperInstance = new RssReaderDbHelper(context);
        }
        return mDbHelperInstance;
    }


}
