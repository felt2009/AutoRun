package com.wfx.autorunner.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.wfx.autorunner.ContextHolder;

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
}
