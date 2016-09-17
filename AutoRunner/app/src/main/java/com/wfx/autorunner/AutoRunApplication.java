package com.wfx.autorunner;

import android.app.Application;

/**
 * Created by n000058 on 2016/9/17.
 */
public class AutoRunApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.initial(this);
    }
}
