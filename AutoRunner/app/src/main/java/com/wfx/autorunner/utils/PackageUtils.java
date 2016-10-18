package com.wfx.autorunner.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.data.AppInfo;
import com.wfx.autorunner.event.UpdateResolveInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sean on 10/18/16.
 */

public class PackageUtils {
    public static boolean scriptInstalled(String packageName) {
        if (packageName == null || TextUtils.isEmpty(packageName))
            return false;
        PackageManager packageManager = ContextHolder.getContext().getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    private static List<AppInfo> sAppInfoList = new ArrayList<>();
    public static AppInfo getAppInfo(String packageName) {
        for (AppInfo appInfo: sAppInfoList) {
            if (appInfo.packageName.equals(packageName)) {
                return appInfo;
            }
        }
        return null;
    }
    public static List<AppInfo> getAppInfoList() {
        try {
            PackageManager pm = ContextHolder.getContext().getPackageManager();
            Intent main = new Intent(Intent.ACTION_MAIN, null);
            main.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(main,
                    0);
            Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(pm));
            sAppInfoList.clear();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                sAppInfoList.add(new AppInfo(resolveInfo.loadLabel(pm).toString(),
                        resolveInfo.activityInfo.packageName, resolveInfo.loadIcon(pm)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new UpdateResolveInfo());
        return sAppInfoList;
    }
}
