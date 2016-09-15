
package com.wfx.autorunner.xposed;

import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook22 implements IXposedHookLoadPackage {
    private static boolean init = false;

    private void readData(LoadPackageParam lpparam) {
        if (!init) {
            try {
                XSharedPreferences pre = new XSharedPreferences(this.getClass()
                        .getPackage().getName(), "prefs");
                String ks[] = {"imei"};
                HashMap<String, String> maps = new HashMap<String, String>();
                for (String k : ks) {
                    String v = pre.getString(k, null);
                    maps.put(k, v);
                    if (TextUtils.isEmpty(v)) {

                        break;
                    }
                }
                if (maps.isEmpty()) {

                } else {
                    HookAll(maps);
                }
            } catch (Throwable e) {

            }
        } else {

        }
    }

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if ("".equals(lpparam.packageName)) {
            return;
        }
        readData(lpparam);

    }

    private void HookAll(final HashMap<String, String> map) {
        HookMethod(TelephonyManager.class, "getDeviceId", map.get("imei"));


    }

    private void HookMethod(final Class cl, final String method,
                            final String result) {
        try {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            param.setResult(result);
                        }

                    }});
        } catch (Throwable e) {
        }
    }


}
