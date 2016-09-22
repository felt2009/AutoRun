package com.wfx.autorunner.data;

import android.graphics.drawable.Drawable;

/**
 * Created by sean on 9/22/16.
 */
public class AppInfo {
    public final String appName;
    public final String packageName;
    public final Drawable appIcon;
    public AppInfo(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
    }
}
