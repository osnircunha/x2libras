package com.ocunha.librasapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by osnircunha on 4/10/16.
 */
public class LibrasApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
