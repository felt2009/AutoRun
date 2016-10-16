package com.wfx.autorunner.xposed;

import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 28147 on 2016/8/1.
 */
public class MainHook implements IXposedHookLoadPackage {

    private XSharedPreferences pre;
    private String thisPackageName = "com.wfx.autorunner";
    static String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        //  readData(loadPackageParam);
        if ("".equals(loadPackageParam.packageName))// System
        {
            return;
        }
        Log.e(TAG, "{" + loadPackageParam.packageName);
        readData(loadPackageParam);
    }


    private void readData(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        pre = new XSharedPreferences(thisPackageName, "prefs");
        String ks[] = {"imei", "mac", "bluemac", "androidid", "osv", "dv", "dm", "opid", "sw", "sh", "lat", "lng"};
        HashMap<String, String> maps = new HashMap<String, String>();
        for (String k : ks) {
            String v = pre.getString(k, null);
            maps.put(k, v);
            if (TextUtils.isEmpty(v)) {
                Log.e(TAG, "{" + loadPackageParam.packageName + "}读取储存内容失败: " + k
                        + " 为Null");
                break;
            } else {
                Log.e(TAG, "Key is " + k + " = " + v);
            }
        }

        if (maps.isEmpty()) {
            Log.e(TAG, "{" + loadPackageParam.packageName
                    + "}读取储存内容失败:  SharedPreferences 为Null");
        } else {
            HookAll(maps, loadPackageParam);
        }
    }

    private void HookAll(final HashMap<String, String> map,
                         XC_LoadPackage.LoadPackageParam lpparam) {
        String packageName = lpparam.packageName;
        ClassLoader classLoader = lpparam.classLoader;
        Log.e(TAG, "Enter HookAll " + packageName);
        // 改变imei
        HookMethod(TelephonyManager.class, "getDeviceId", map.get("imei"), 1);
        HookMethod(android.net.wifi.WifiInfo.class, "getMacAddress", map.get("mac"), 1);// mac地址
        // 蓝牙地址
        HookMethod(BluetoothAdapter.class, "getAddress", map.get("bluemac"), 1);
        // 改变android_id(有两个获取android_id的方法)
        HookMethod(android.provider.Settings.Secure.class, "getString",
                map.get("androidid"), 4);
        HookMethod(android.provider.Settings.System.class, "getString",
                map.get("androidid"), 4);

        try {
            if (map.get("osv") != null) {
                XposedHelpers.findField(android.os.Build.VERSION.class,
                        "RELEASE").set(null, map.get("osv"));// 系统版本
            }

            if (pre.getString("dv", null) != null)
                XposedHelpers.findField(android.os.Build.class, "MANUFACTURER")
                        .set(null, pre.getString("dv", ""));// 制造商

            if (map.get("dm") != null)
                XposedHelpers.findField(android.os.Build.class, "MODEL").set(
                        null, map.get("dm"));// 机型

        } catch (Exception e) {
            e.printStackTrace();
        }

        HookMethod(TelephonyManager.class, "getSimOperator", map.get("opid"), 1);// 运营商
        // 屏幕宽高
        HookMethod(Display.class, "getHeight", map.get("fbl_h"), 2);
        HookMethod(Display.class, "getWidth", map.get("fbl_w"), 2);

        //分辨率

        try {
            XposedHelpers.findAndHookMethod(Display.class, "getMetrics",
                    new Object[]{android.util.DisplayMetrics.class,
                            new XC_MethodHook() {
                                protected void afterHookedMethod(
                                        MethodHookParam param) throws Throwable {

                                    // onLog("TTT--修改分辨率 前！");

                                    android.util.DisplayMetrics ddm = (android.util.DisplayMetrics) param.args[0];

                                    if (map.get("fbl_h") != null
                                            && map.get("fbl_h").length() > 0)// 屏幕分辨率_高
                                        ddm.heightPixels = Integer.valueOf(map
                                                .get("fbl_h"));

                                    if (map.get("fbl_w") != null
                                            && map.get("fbl_w").length() > 0)// 屏幕分辨率_宽
                                        ddm.widthPixels = Integer.valueOf(map
                                                .get("fbl_w"));

                                    if (map.get("density") != null
                                            && map.get("density").length() > 0)
                                        ddm.density = Float.valueOf(map
                                                .get("density"));

                                    if (map.get("scaledDensity") != null
                                            && map.get("scaledDensity")
                                            .length() > 0)
                                        ddm.scaledDensity = Float.valueOf(map
                                                .get("scaledDensity"));

                                    if (map.get("densityDpi") != null
                                            && map.get("densityDpi").length() > 0)
                                        ddm.densityDpi = Integer.valueOf(map
                                                .get("densityDpi"));

                                    if (map.get("xdpi") != null
                                            && map.get("xdpi").length() > 0)
                                        ddm.xdpi = Float.valueOf(map
                                                .get("xdpi"));

                                    if (map.get("ydpi") != null
                                            && map.get("ydpi").length() > 0)
                                        ddm.ydpi = Float.valueOf(map
                                                .get("ydpi"));

                                    param.setResult(ddm);
                                }

                            }});
        } catch (Throwable e) {

        }

        // 需要在wifi开启的情况下才能修改成功GPS经纬度，否则都会是0；
        HookMethod(Location.class, "getLatitude", map.get("lat"), 0);
        HookMethod(Location.class, "getLongitude", map.get("lng"), 0);
    }


    private void HookMethod(final Class cl, final String method,
                            final Object result, final int type) {
        try {

            Object[] obj = null;

            if (result == null) {
                Log.e(TAG, "Exit because result = null Method is " + method);
                return;
            }

            if (String.valueOf(result).length() == 0) {
                Log.e(TAG, "Exit because result.Length = null");
                return;
            }
            Log.e(TAG, "Good here " + type + method);
            if (type == 4) {

                // 修改android_id
                obj = new Object[]{android.content.ContentResolver.class, String.class,
                        new XC_MethodHook() {

                            protected void afterHookedMethod(
                                    MethodHookParam param) throws Throwable {
                                String aid = (String) result;

                                Log.e(TAG, "at hook method " + type + param.method.getName());
                                if (param.args.length >= 2
                                        && param.args[1] == "android_id"
                                        && aid.length() > 0) {
                                    Log.e(TAG, "Good here 4 " + result);
                                    param.setResult(aid);
                                }

                            }

                        }};

            } else {


                obj = new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        Log.e(TAG, "at hook method " + type);
                        switch (type) {
                            case 0:
                                Log.e(TAG, "Good here 0 " + result);
                                param.setResult(Double.valueOf(result + ""));
                                break;
                            case 1:
//                                Log.e(TAG, "Good here 1 " + result + " param.args[0] " + param.args[0].toString() + " param is  "
//                                        + param.toString() + " Method  " + param.method.toString());
                                Log.e(TAG, "Good here 1 " + result + " param is  "
                                        + param.toString() + " Method  " + param.method.toString());
                                param.setResult(result);
                                break;
                            case 2:
                                Log.e(TAG, "Good here 2 " + result);
                                param.setResult(Integer.parseInt(result + ""));
                                break;
                        }
                    }

                }};

            }

            de.robv.android.xposed.XposedHelpers.findAndHookMethod(cl, method, obj);
        } catch (Throwable e) {
            Log.e(TAG, "修改" + method + "失败!" + e.getMessage());
        }
    }
}
