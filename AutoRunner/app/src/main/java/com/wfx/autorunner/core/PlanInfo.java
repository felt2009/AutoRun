package com.wfx.autorunner.core;


/**
 * Created by sean on 9/13/16.
 */
public class PlanInfo implements Comparable<PlanInfo> {
    private static final String TAG = "PlanInfo";

    @Override
    public int compareTo(PlanInfo planInfo) {
        return ((Long)ts).compareTo((planInfo.ts));
    }

    public enum Status {
        stop(0),
        running(1);
        public final int value;
        Status(int value) {
            this.value = value;
        }
    }

    private String name;        // plan name, default set as package name of target app
    //private List<TaskEntry> tasks;
    //private String path;
    private long ts;            // plan ts, create time & date
    private Script script;      // chosen script
    private int totalCount;     // total execute count
    private int count;          // current executed count
    private int status;         // plan running status
    public PlanInfo(String name, long ts, Script script, int totalCount) {
        this.name = name;
        this.ts = ts;
        this.script = script;
        this.status = Status.stop.value;
        this.totalCount = totalCount;
        count = 0;
    }
    public PlanInfo(String name, long ts, Script script, int totalCount, int count, int status) {
        this.name = name;
        this.ts = ts;
        this.script = script;
        this.totalCount = totalCount;
        this.count = count;
        this.status = status;
    }

    /*
    public PlanInfo(String name, String path, long ts) {
        this.path = path;
        this.name = name;
        this.ts = ts;
    }*/
    public int getTotalCount() { return totalCount; }
    public int getStatus() { return status; }
    public int getCount() { return count; }
    public Script getScript() { return script; }
    public long getTs() {
        return ts;
    }
    public String getName() {
        return name;
    }
    /*public String getPath() {
        return path;
    }

    public List<TaskEntry> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntry> tasks) {
        this.tasks = tasks;
    }*/

    public TaskEntry getNextTaskEntry() {
        /*
        for(int i = 0 ; i < tasks.size() ; i++) {
            TaskEntry entry = tasks.get(i);
            if(entry.getRepeatCount() > 0) {
                entry.setRepeatCount(entry.getRepeatCount() - 1);
                Log.d(TAG, "i = " + i + " count = " + entry.getRepeatCount());
                return entry;
            }
        }*/
        return (count++ < totalCount) ? new TaskEntry(name, script, totalCount) : null;
    }
}
