package com.wfx.autorunner.core;

/**
 * Created by sean on 9/27/16.
 */
public class Script {
    private String scriptName;
    private int time;
    private int type;
    public String getScriptName() {
        return scriptName;
    }
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
