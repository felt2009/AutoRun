package com.wfx.autorunner.controller;

import android.util.Log;

import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.Script;
import com.wfx.autorunner.core.ScriptInfo;
import com.wfx.autorunner.core.TaskEntry;
import com.wfx.autorunner.db.DataBaseManager;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;

/**
 * Created by joe on 2016/10/11.
 */
public class PlanHelper {
    static private String TAG = "PlanHelper";
    static boolean bStop = false;

    static public void runPlan(PlanInfo planInfo) {
        Log.i(TAG, "running" + planInfo.getName());
        ScriptRunning running = new ScriptRunning();
        TaskEntry entry = planInfo.getNextTaskEntry();
        bStop = false;
        while (entry != null && !bStop) {
            running.setScript(entry.getScript());
            running.setTargetPackageName(entry.getTargetPackageName());

            // TODO area need to be get;
            PhoneInfoHelper.getInstance().generatePhoneInfo(entry.getTargetPackageName(), "area", entry.getScript().getType());
            running.prepare();
            if (PhoneInfoHelper.getInstance().waitPhoneInfoValid(20)) {
                running.runScript();
            } else {
                Log.i(TAG, "FAILED, Phone info not valid.");
            }
            entry = planInfo.getNextTaskEntry();
        }
    }

    static public void stopRun() {
        bStop = true;
    }
}
