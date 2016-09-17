package com.wfx.autorunner.tester;

import com.wfx.autorunner.controller.PhoneInfoHelper;
import com.wfx.autorunner.controller.ScriptRunning;
import com.wfx.autorunner.core.ScriptInfo;

/**
 * Created by n000058 on 2016/9/17.
 */
// FIXME Just for testing
public class MyTester {
    public void testRunScript() {
        final ScriptRunning running = new ScriptRunning();
        running.setScriptInfo(new ScriptInfo(getTestScriptPackage(),getTestPackage(),"",getTestTime(),ScriptInfo.CATEGORY_FIRST_LAUNCH));
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                running.runScript();
            }
        });
        thread.start();
    }

    public void testRunInstalledPhoneInfo() {
        PhoneInfoHelper.getInstance().generatePhoneInfo(PhoneInfoHelper.TYPE_INSTALL);
    }

    public void testRunOpenedPhoneInfo() {
        PhoneInfoHelper.getInstance().generatePhoneInfo(PhoneInfoHelper.TYPE_OPEN);
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
}
