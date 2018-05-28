package com.mree.inc.track;

import android.app.Application;
import android.content.Context;

import com.mree.inc.track.db.AppDatabase;

public class TrackApp extends Application {
    private static Context context;
    private static AppDatabase appDatabase;
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static AppDatabase getDatabase() {
        if (appDatabase == null)
            appDatabase = AppDatabase.getDatabase(context);

        return appDatabase;
    }
}
