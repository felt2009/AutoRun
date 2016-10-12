package com.wfx.autorunner.core;

/**
 * Created by sean on 9/13/16.
 */
// ToDO need remove;
public class ScriptInfo {
    public final static int CATEGORY_RETENTION = 0;
    public final static int CATEGORY_FIRST_LAUNCH = 1;
    private final String scriptPackageName;
    private final String targetPackageName;
    private String scriptPath;
    private int runningTime;
    private int category;
    public ScriptInfo(String scriptPackageName, String targetPackageName,
                      String scriptPath, int runningTime, int category) {
        this.scriptPackageName = scriptPackageName;
        this.targetPackageName = targetPackageName;
        this.scriptPath = scriptPath;
        this.runningTime = runningTime;
        this.category = category;
    }
    public String getScriptPackageName() {
        return scriptPackageName;
    }
    public String getTargetPackageName() {
        return targetPackageName;
    }
    public String getScriptPath() {
        return scriptPath;
    }
    public int getRunningTime() {
        return runningTime;
    }
    public int getCategory() {
        return category;
    }
}
