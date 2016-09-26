package com.wfx.autorunner.core;

/**
 * Created by sean on 9/13/16.
 */
public class TaskEntry {
    private TargetApp targetApp;
    private ScriptInfo scriptInfo;
    private int repeatCount;

    public void setTargetApp(TargetApp targetApp) {
        this.targetApp = targetApp;
    }

    public void setScriptInfo(ScriptInfo scriptInfo) {
        this.scriptInfo = scriptInfo;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public TargetApp getTargetApp() {
        return targetApp;
    }

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
