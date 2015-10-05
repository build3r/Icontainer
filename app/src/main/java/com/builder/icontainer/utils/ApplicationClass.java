package com.builder.icontainer.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shabaz on 29-Aug-15.
 */
public class ApplicationClass extends Application
{
    public static Context static_context;
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        static_context = this.getApplicationContext();
    }
}
