package com.wfx.autorunner.core;

/**
 * Created by sean on 9/13/16.
 */
public class TargetApp {
    private final String name;
    private final String packageName;
    private final String path;
    public TargetApp(String name, String packageName, String path) {
        this.name = name;
        this.packageName = packageName;
        this.path = path;
    }
    public String getName() {
        return name;
    }
    public String getPackageName() {
        return packageName;
    }
    public String getPath() {
        return path;
    }
}
