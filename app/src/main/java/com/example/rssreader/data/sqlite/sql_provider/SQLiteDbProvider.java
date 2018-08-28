package com.example.rssreader.data.sqlite.sql_provider;

import com.example.rssreader.data.sqlite.RssReaderDbHelper;

public class SQLiteDbProvider implements IDbProvider {

//    private final WeakReference<Context> mContextWeakReference;
//
//    public SQLiteDbProvider(Context context){
//        mContextWeakReference = new WeakReference<>(context);
//    }

    @Override
    public RssReaderDbHelper provide() {
        return RssReaderDbHelper.getDbHelperInstance();
    }
}
