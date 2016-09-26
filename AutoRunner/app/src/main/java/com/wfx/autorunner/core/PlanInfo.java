package com.wfx.autorunner.core;

import android.util.Log;

import java.util.List;

/**
 * Created by sean on 9/13/16.
 */
public class PlanInfo {
    static final String TAG = "PlanInfo";
    private String name;
    private List<TaskEntry> tasks;
    private String path;
    private long ts;
    public PlanInfo(String name, String path, long ts) {
        this.path = path;
        this.name = name;
        this.ts = ts;
    }
    public long getTs() {
        return ts;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }

    public List<TaskEntry> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntry> tasks) {
        this.tasks = tasks;
    }

    public TaskEntry getNextTaskEntry() {
        for(int i = 0 ; i < tasks.size() ; i++) {
            TaskEntry entry = tasks.get(i);
            if(entry.getRepeatCount() > 0) {
                entry.setRepeatCount(entry.getRepeatCount() - 1);
                Log.d(TAG, "i = " + i + " count = " + entry.getRepeatCount());
                return entry;
            }
        }
        return null;
    }
}
