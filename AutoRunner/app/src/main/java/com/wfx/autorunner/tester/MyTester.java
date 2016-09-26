package com.wfx.autorunner.tester;

import android.util.Log;

import com.wfx.autorunner.controller.PhoneInfoHelper;
import com.wfx.autorunner.controller.ScriptRunning;
import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.ScriptInfo;
import com.wfx.autorunner.core.TaskEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 2016/9/17.
 */
// FIXME Just for testing
public class MyTester {
    public final String TAG = "MyTest";
    PlanInfo planInfo = new PlanInfo("test", "test",100);
    public void testRunScript() {
        final ScriptRunning running = new ScriptRunning();
        running.setScriptInfo(new ScriptInfo(getTestScriptPackage(),getTestPackage(),"",getTestTime(),ScriptInfo.CATEGORY_FIRST_LAUNCH));
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                PhoneInfoHelper.getInstance().generatePhoneInfo("test", "test",PhoneInfoHelper.TYPE_INSTALL);
                running.prepare();
                if(PhoneInfoHelper.getInstance().waitPhoneInfoValid(20)) {
                    running.runScript();
                } else {
                    Log.i(TAG, "FAILED, Phone info not valid.");
                }
            }
        });
        thread.start();
    }

    public void testRunPlan() {
       Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                ScriptRunning running = new ScriptRunning();
                generatePlanInfo();
                TaskEntry entry = planInfo.getNextTaskEntry();
                while(entry != null) {
                    running.setScriptInfo(entry.getScriptInfo());
                    PhoneInfoHelper.getInstance().generatePhoneInfo("package", "area",PhoneInfoHelper.TYPE_INSTALL);
                    running.prepare();
                    if(PhoneInfoHelper.getInstance().waitPhoneInfoValid(20)) {
                        running.runScript();
                    } else {
                        Log.i(TAG, "FAILED, Phone info not valid.");
                    }
                    entry = planInfo.getNextTaskEntry();
//                    Log.i(TAG, "");
                }
            }
        });
        thread.start();
    }

    public void testRunOneProcess() {

    }

    public void testRunInstalledPhoneInfo() {
        PhoneInfoHelper.getInstance().generatePhoneInfo("test", "test",PhoneInfoHelper.TYPE_INSTALL);
        Log.i(TAG,"in Test Installed Phone Info " + PhoneInfoHelper.getInstance().getPhoneInfo().toString());
    }

    public void testRunOpenedPhoneInfo() {
        PhoneInfoHelper.getInstance().generatePhoneInfo("test", "test",PhoneInfoHelper.TYPE_OPEN);
        Log.i(TAG, "in Test Open Phone Info " + PhoneInfoHelper.getInstance().getPhoneInfo().toString());
    }

    // 获取要执行的脚本包名 TODO
    private String getTestScriptPackage() {
        return "com.ngmlmemjmpmcnimlme.drivea";
    }

    // 获取测试Apk的包名 TODO
    private String getTestPackage() {
        return "com.handsgo.jiakao.android";
    }
    private int getTestTime() { return 45000; }

    // 生成计划
    private void generatePlanInfo() {
        ScriptInfo script1 = new ScriptInfo(getTestScriptPackage(),getTestPackage(),"",getTestTime(),ScriptInfo.CATEGORY_FIRST_LAUNCH);
        TaskEntry entry1 = new TaskEntry();
        entry1.setScriptInfo(script1);
        entry1.setRepeatCount(2);
        List<TaskEntry> entryList = new ArrayList<TaskEntry>();
        entryList.add(entry1);
        ScriptInfo script2 = new ScriptInfo(getTestScriptPackage(),getTestPackage(),"",getTestTime(),ScriptInfo.CATEGORY_FIRST_LAUNCH);
        TaskEntry entry2 = new TaskEntry();
        entry2.setScriptInfo(script1);
        entry2.setRepeatCount(1);
        entryList.add(entry2);
        planInfo.setTasks(entryList);
    }
}
