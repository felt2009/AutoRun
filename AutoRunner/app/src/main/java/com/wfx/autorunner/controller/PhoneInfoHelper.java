package com.wfx.autorunner.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.core.Environment;
import com.wfx.autorunner.network.ServerApiManager;
import com.wfx.autorunner.xposed.RandomData;

import org.json.JSONObject;

/**
 * Created by joe on 2016/9/15.
 */
public class PhoneInfoHelper {
    static String TAG = "PhoneInfoHelper";

    public final static int TYPE_OPEN = 0;
    public final static int TYPE_INSTALL = 1;
    private SharedPreferences preferences;
//    private String strIP,strIntentAddress;

    static private PhoneInfoHelper mInstance = null;
    Environment environment = null;

    private PhoneInfoHelper() {}
    public static PhoneInfoHelper getInstance() {
        if(mInstance == null) {
            mInstance = new PhoneInfoHelper();
        }
        return mInstance;
    }

    private Context getContext() {
        return ContextHolder.getContext();
    }

    public boolean waitPhoneInfoValid(int seconds) {
        if(seconds < 0 || seconds > 20) {
            seconds = 20;
        }
        for(int i = 0 ; i < seconds ; i++) {
            if(environment != null) {
                Log.i(TAG, "Get Phone info after " + i*1000 + " Seconds.");
                return true;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG, "Cannot get Phoneinfo, something wrong, maybe network problem.");
        return false;
    }

    // 从网络中获取手机信息；
    public void generatePhoneInfo(String packageName, String area, int type) {
        environment = null;
        if(type == TYPE_OPEN) {
            // 留存
            generateRetentionPhoneInfo(packageName, area);
        } else if(type == TYPE_INSTALL) {
            // 激活
            generateInstallPhoneInfoInteral(packageName, area);
        }
    }

    private void generateInstallPhoneInfoInteral(final String packageName, final String area){
        ServerApiManager.instance().fetchEnvironmentInfo(new ServerApiManager.Listener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinished(boolean success, JSONObject response) {
                Log.d("SEAN", "onFinished:" + response.toString());
                environment = JSON.parseObject(response.toString(), Environment.class);
                updatePreference();
            }
        }, packageName, area, "fetchEnveironment_"+packageName);
    }

    private void generateRetentionPhoneInfo(final String packageName, final String area){
        ServerApiManager.instance().fetchRetentionInfo(new ServerApiManager.Listener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinished(boolean success, JSONObject response) {
                Log.d("SEAN", "onFinished:" + response.toString());
                environment = JSON.parseObject(response.toString(), Environment.class);
                updatePreference();
            }
        }, packageName, area, "fetchRetention_"+packageName);
    }

    private void updatePreference() {
        SharedPreferences pre = (SharedPreferences) ContextHolder.getContext().getSharedPreferences("prefs",
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor= pre.edit();
        editor.putString("imei", String.valueOf(environment.getImei()));
        editor.putString("mac", environment.getMac());
        editor.putString("bluemac", environment.getBlue_mac());
        editor.putString("androidid",  environment.getAndroid_id());
        editor.putString("imsi",environment.getImsi());
        editor.putString("opid", environment.getOpid());
        editor.putString("osv", environment.getOsv());
        editor.putString("dv", environment.getDv());
        editor.putString("dm", environment.getDm());

        // FIXME
        int densityDpi = RandomData.getDensityDpi(String.valueOf(environment.getSw()));
        float density = RandomData.getDensity(String.valueOf(environment.getSh()));
        float xdpi = RandomData.getDpi(densityDpi);
        float ydpi = RandomData.getDpi(densityDpi);

        editor.putString("fbl_w", String.valueOf(environment.getSw()));// 分辨率-宽
        editor.putString("fbl_h", String.valueOf(environment.getSh()));// 分辨率-高
        editor.putString("density", density + "");
        editor.putString("densityDpi", densityDpi + "");
        editor.putString("scaledDensity", density + "");
        editor.putString("xdpi", xdpi + "");
        editor.putString("ydpi", ydpi + "");
        editor.putString("sw",String.valueOf(environment.getSw()));
        editor.putString("sh", String.valueOf(environment.getSh()));
        editor.apply();
        Log.i(TAG,"Current Environment " + environment.toString());
    }
//    public void getIP(String paramString){
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        // 2 创建StringRequest对象
//        MyStringRequest mStringRequest = new MyStringRequest(paramString,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(!TextUtils.isEmpty(response)){
//                            Log.e(TAG, response);
//                            if (response.contains("您的IP是")) {
//                                int k = 1 + response.indexOf('[');
//                                int m = response.indexOf(']');
//
//                                strIP = response.substring(k, m);
//
////                                Toast.makeText(TestActivity.this,strIP,Toast.LENGTH_SHORT).show();
//
//                            }
//                            if (response.contains("来自")) {
//                                int i = 3 + response.indexOf("来自：");
//                                int j = response.indexOf("</center>");
//                                strIntentAddress = response
//                                        .substring(i, j);
//                                Log.e(TAG, strIntentAddress);
////                                Toast.makeText(TestActivity.this,strIntentAddress,Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("请求错误:" + error.toString());
//            }
//        });
//        // 3 将StringRequest添加到RequestQueue
//        requestQueue.add(mStringRequest);
//    }

    // 获取手机里所有的包名 TODO
//    public String getAllPackageStr() {
//        List<String> list = new ArrayList<String>();
//
//        List<PackageInfo> packs = getContext().getPackageManager()
//                .getInstalledPackages(0);
//        int size = packs.size();
//
//        for (PackageInfo packageInfo : packs) {
//            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//
//            if (isSystemApp(applicationInfo)) {
//
//                list.add(packageInfo.packageName);
//            } else {
//
//            }
//        }
//
//        StringBuilder builder = new StringBuilder();
//        for (String s : list) {
//            builder.append(s + "<;>");
//        }
//        return builder.substring(0, builder.length() - 1).toString();
//    }
//    private static boolean isSystemApp(ApplicationInfo appInfo) {
//        return (appInfo.flags & appInfo.FLAG_SYSTEM) > 0;
//    }

}
