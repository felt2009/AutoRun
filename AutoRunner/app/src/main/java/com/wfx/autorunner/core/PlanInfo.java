package com.wfx.autorunner.core;

import java.util.List;

/**
 * Created by sean on 9/13/16.
 */
public class PlanInfo {
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
}
