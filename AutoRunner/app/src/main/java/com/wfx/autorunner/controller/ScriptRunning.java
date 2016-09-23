package com.wfx.autorunner.controller;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.core.ScriptInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by joe on 2016/9/17.
 */
public class ScriptRunning {
    static String TAG = "ScriptRunning";
    private Process process;

    private ScriptInfo scriptInfo;

    public void setScriptInfo(ScriptInfo scriptInfo) {
        this.scriptInfo = scriptInfo;
    }

    public boolean runScript() {
        if(scriptInfo != null) {
            return runScript(scriptInfo.getScriptPackageName(),scriptInfo.getTargetPackageName(),scriptInfo.getRunningTime());
        }
        return false;
    }

    // 取得root权限；
    private void requestSu() {
        try {
            process = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 按下Volume down 键 FIXME /dev/input/event0 need change to real device.
    private void runVolumeDownKey() {
        execShellCmd("sendevent /dev/input/event0 1 114 1");
        sleep(100);
        execShellCmd("sendevent /dev/input/event0 0 0 0");
        sleep(100);
        execShellCmd("sendevent /dev/input/event0 1 114 0");
        sleep(100);
        execShellCmd("sendevent /dev/input/event0 0 0 0");
    }

    // sleep with no exception
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 执行Shell脚本
    private void execShellCmd(String cmd) {
        requestSu();
        OutputStream outputStream = process.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(
                outputStream);
        try {
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 下载脚本APK和相关信息
    private void downloadScripts() {
        // TODO
        Log.i(TAG, "downloadScripts" );
    }

    // 执行脚本, 返回值 true 正常执行， false，没有执行；
    private boolean runScript(String scriptPackage, String testPackage,int runningTime) {
        killRunningPackageWithShell(testPackage);
        sleep(1000);
        killRunningPackageWithShell(scriptPackage);
        sleep(2000);
        runPackage(scriptPackage);
        sleep(8000);
        runVolumeDownKey();
        sleep(3000);
        runPackage(testPackage);
        sleep(5000);
        if(isAppInFront(testPackage)) {
            Log.i(TAG, "test Package is in front " + testPackage);
        } else {
            Log.i(TAG, "test Package is not in Front " + testPackage);
            return false;
        }
        sleep(runningTime);
        return true;
    }

    // 获取要执行的脚本包名 TODO
    private String getTestScriptPackage() {
        return "com.ngmlmemjmpmcnimlme.drivea";
    }

    // 获取测试Apk的包名 TODO
    private String getTestPackage() {
        return "com.handsgo.jiakao.android";
    }
    // 执行APK
    private void runPackage(String packageName) {
        Log.i(TAG, packageName);
        PackageManager packageManager = getContext().getPackageManager();
        Intent it= packageManager.getLaunchIntentForPackage(packageName);
        if(it==null) {
            Log.i(TAG, "APP not found!");
            return;
        }
        Log.i(TAG, "IT get " + it.getComponent());
        getContext().startActivity(it);
    }

    // 杀掉进程，
    private void killRunningPackageWithShell(String packageName) {
        execShellCmd("am force-stop "+ packageName);
        sleep(500);
        // 2次保证杀死的概率高些；
        execShellCmd("am force-stop "+ packageName);
    }

    // 判断包是否在前台；
    private boolean isAppInFront(String packageName) {
        ActivityManager activityManager =(ActivityManager) getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private Context getContext() {
        return ContextHolder.getContext();
    }

    // 安装脚本APK
    private void installScripts() {
        // TODO
        Log.i(TAG, "installScripts");
    }

    // 获取手机信息，并存储
    private void getPhoneInfo() {

    }

    // 获取VPN信息并存储
    private void getVPNInfo() {

    }

    // 设置VPN
    private void setVPN() {
//        runScript("com.cedecjaididhbkemdkda.test", "com.android.settings", 50000);
    }

    // FIXME just for test need remove;
    public void runTestFlow() {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                downloadScripts();
                installScripts();
                getPhoneInfo();
                getVPNInfo();
                setVPN();
                // TODO Just for test;
                int wrong = 0;
                int count = 30;
                for(int i = 0 ; i < count ; i++) {
                    Log.i(TAG, "Total time is " + count + " time is " + i + " total is " + count);
                    if(runScript(getTestScriptPackage(), getTestPackage(),40000)) {
                        Log.i(TAG, "Run well");
                    } else {
                        wrong++;
                        i--;
                        Log.i(TAG, "Error, need Run again");
                        Log.i(TAG, "Total time is " + count + " " + wrong + " times not run");
                        if(count < wrong * 3) {
                            Log.e(TAG, "Too many wrong running, stop running");
                            Log.e(TAG, "Total time is " + count + " " + wrong + " times not run");
                            break;
                        }
                    }
                }
                Log.i(TAG, "Total time is " + count + " " + wrong + " times not run");
            }
        });
        thread.start();
    }

    // Swipe to Next screen Not use here TODO
    private void SwipeToNextScreen() {
        execShellCmd("input swipe 500 300 40 300  ");
    }
    // Swipe to prev screen Not use here TODO
    private void SwipeToPrevScreen() {
        execShellCmd("input swipe 50 300 500 300  ");
    }
    // 点击某个点；Not use here TODO
    private void tap(int x, int y) {
        execShellCmd("input tap " + x + " " + y);
    }
    // 按下home键 Not use here TODO
    private void runHomeKey() {
        execShellCmd("input keyevent 3");
    }
}
