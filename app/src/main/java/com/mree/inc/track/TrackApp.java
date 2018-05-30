package com.mree.inc.track;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.mree.inc.track.db.AppDatabase;

public class TrackApp extends MultiDexApplication {
    private static Context context;
    private static AppDatabase appDatabase;

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDatabase() {
        if (appDatabase == null)
            appDatabase = AppDatabase.getDatabase(context);

        return appDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
