package com.wfx.autorunner.core;

/**
 * Created by sean on 9/13/16.
 */
public class TaskEntry {
    private String targetPackageName;
    private Script script;
    private int repeatCount;

    public TaskEntry(String targetPackageName, Script script, int repeatCount) {
        this.targetPackageName = targetPackageName;
        this.script = script;
        this.repeatCount = repeatCount;
    }

    public Script getScript() {
        return script;
    }

    public String getTargetPackageName() {
        return targetPackageName;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setTargetPackageName(String targetPackageName) {
        this.targetPackageName = targetPackageName;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setScript(Script script) {
        this.script = script;
    }
}
