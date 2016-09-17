package com.wfx.autorunner;

import android.content.Context;

/**
 * Created by n000058 on 2016/9/17.
 */
public class ContextHolder {
    static Context ApplicationContext;
    public static void initial(Context context) {
        ApplicationContext = context;
    }
    public static Context getContext() {
        return ApplicationContext;
    }
}
