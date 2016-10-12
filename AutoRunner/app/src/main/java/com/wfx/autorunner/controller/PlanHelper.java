package com.wfx.autorunner.controller;

import android.util.Log;

import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.Script;
import com.wfx.autorunner.core.ScriptInfo;
import com.wfx.autorunner.core.TaskEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 2016/10/11.
 */
public class PlanHelper {
    static private String TAG = "PlanHelper";

    static public PlanInfo generatePlan(String targetPackageName, List<Script> scripts, int totalCount) {
        List<TaskEntry> entryList = new ArrayList<TaskEntry>();
        int average = totalCount / scripts.size();
        int count = 0;
        Log.i(TAG, "size is " + scripts.size() + " average is " + average + " total Count is " + totalCount);
        for(int i = 0 ; i < scripts.size() ; i++) {
            Script s = scripts.get(i);
            TaskEntry entry;
            // TODO need check script package exist;
            if(i == scripts.size() - 1) {
                // last script,
                entry = new TaskEntry(targetPackageName, s, totalCount);
            } else {
                entry = new TaskEntry(targetPackageName, s, average);
                count += average;
            }
            entryList.add(entry);
        }
        PlanInfo planInfo = new PlanInfo("test","test",100);
        planInfo.setTasks(entryList);
        return planInfo;
    }

    static public void runPlan(PlanInfo planInfo) {
        Log.i(TAG, "running" + planInfo.getTasks().get(0).toString());
        ScriptRunning running = new ScriptRunning();
        TaskEntry entry = planInfo.getNextTaskEntry();
        while (entry != null) {
            running.setScript(entry.getScript());
            running.setTargetPackageName(entry.getTargetPackageName());
            // FIXME type 只能是留存或者激活，依据界面的选择;
            // TODO area need get;
            PhoneInfoHelper.getInstance().generatePhoneInfo(entry.getTargetPackageName(), "area", entry.getScript().getType());
            running.prepare();
            // FIXME need undo comment, when environment can get from internet;
//            if (PhoneInfoHelper.getInstance().waitPhoneInfoValid(20)) {
                running.runScript();
//            } else {
//                Log.i(TAG, "FAILED, Phone info not valid.");
//            }
            entry = planInfo.getNextTaskEntry();
        }
    }
}
