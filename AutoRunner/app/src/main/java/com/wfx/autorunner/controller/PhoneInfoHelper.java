package com.wfx.autorunner.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.xposed.MyStringRequest;
import com.wfx.autorunner.xposed.RandomData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by n000058 on 2016/9/15.
 */
public class PhoneInfoHelper {
    static String TAG = "PhoneInfoHelper";
    // installed URL 留存？
    private static final String URL_INSTALL ="http://106.75.148.113:80/v1/install";
    // open URL 新用户？
    private static final String URL_OPEN ="http://106.75.148.113:80/v1/open";

    public final static int TYPE_OPEN = 1;
    public final static int TYPE_INSTALL = 2;

    private String imei,mac,blue_mac,android_id,imsi,osv,dv,dm,opid,sw,sh,lat,lng;
    private SharedPreferences preferences;
    private String strIP,strIntentAddress;

    static private PhoneInfoHelper mInstance = null;
    public final static int STATUS_NOT_STARTED = 1;
    public final static int STATUS_IN_PROCESS = 2;
    public final static int STATUS_GOT_INFO = 3;
    private int mStatus = STATUS_NOT_STARTED;

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

    // 从网络中获取手机信息；
    public void generatePhoneInfo(int type) {
        mStatus = STATUS_IN_PROCESS;
        if(type == TYPE_OPEN) {
            getOpenedPhoneInfos();
        } else if(type == TYPE_INSTALL) {
            getInstallPhoneInfos();
        }
    }

    private void saveData() {
        preferences = getContext().getSharedPreferences("prefs",
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor pre = preferences.edit();
        String str =getAllPackageStr();
        pre.putString("hhlpackagename", str);
        pre.putString("imei", imei);
        pre.putString("mac", mac);
        pre.putString("bluemac", blue_mac);
        pre.putString("androidid",  android_id);
        pre.putString("imsi",imsi);
        pre.putString("opid", opid);
        pre.putString("osv", osv);
        pre.putString("dv", dv);
        pre.putString("dm", dm);

        // 分辨率相关参数
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaymetrics);

        int densityDpi = displaymetrics.densityDpi;
        float density = displaymetrics.density;
        float xdpi = displaymetrics.xdpi;
        float ydpi = displaymetrics.ydpi;

        //   if (preferences.getBoolean("isDensity", false)) {
        densityDpi = RandomData.getDensityDpi(sw);
        density = RandomData.getDensity(sh);
        xdpi = RandomData.getDpi(densityDpi);
        ydpi = RandomData.getDpi(densityDpi);
        //  }

        pre.putString("fbl_w", sw);// 分辨率-宽
        pre.putString("fbl_h", sh);// 分辨率-高
        pre.putString("density", density + "");
        pre.putString("densityDpi", densityDpi + "");
        pre.putString("scaledDensity", density + "");
        pre.putString("xdpi", xdpi + "");
        pre.putString("ydpi", ydpi + "");
        pre.putString("sw",sw);
        pre.putString("sh", sh);
        pre.apply();
    }
    // 获取手机里所有的包名
    public String getAllPackageStr() {
        List<String> list = new ArrayList<String>();

        List<PackageInfo> packs = getContext().getPackageManager()
                .getInstalledPackages(0);
        int size = packs.size();

        for (PackageInfo packageInfo : packs) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;

            if (isSystemApp(applicationInfo)) {

                list.add(packageInfo.packageName);
            } else {

            }
        }

        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s + "<;>");
        }
        return builder.substring(0, builder.length() - 1).toString();
    }
    private static boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & appInfo.FLAG_SYSTEM) > 0;
    }

    public void getInstallPhoneInfos(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_INSTALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                imei = jsonObject.getString("imei");
                                mac = jsonObject.getString("mac");
                                blue_mac = jsonObject.getString("blue_mac");
                                android_id = jsonObject.getString("android_id");
                                imsi = jsonObject.getInt("imsi")+"";
                                opid = jsonObject.getInt("opid")+"";
                                osv = jsonObject.getString("osv");
                                dv = jsonObject.getString("dv");
                                dm = jsonObject.getString("dm");
                                sw = jsonObject.getInt("sw")+"";
                                sh = jsonObject.getInt("sh")+"";
                                // FIXME
                                saveData();
                                mStatus = STATUS_GOT_INFO;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("packagename", "123");
                params.put("area", "123123");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getOpenedPhoneInfos(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_OPEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                imei = jsonObject.getString("imei");
                                mac = jsonObject.getString("mac");
                                blue_mac = jsonObject.getString("blue_mac");
                                android_id = jsonObject.getString("android_id");
                                imsi = jsonObject.getInt("imsi")+"";
                                opid = jsonObject.getInt("opid")+"";
                                osv = jsonObject.getString("osv");
                                dv = jsonObject.getString("dv");
                                dm = jsonObject.getString("dm");
                                sw = jsonObject.getInt("sw")+"";
                                sh = jsonObject.getInt("sh")+"";
                                saveData();
                                mStatus = STATUS_GOT_INFO;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("packagename", "123");
                params.put("area", "123123");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getIP(String paramString){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // 2 创建StringRequest对象
        MyStringRequest mStringRequest = new MyStringRequest(paramString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            Log.e(TAG, response);
                            if (response.contains("您的IP是")) {
                                int k = 1 + response.indexOf('[');
                                int m = response.indexOf(']');

                                strIP = response.substring(k, m);

//                                Toast.makeText(TestActivity.this,strIP,Toast.LENGTH_SHORT).show();

                            }
                            if (response.contains("来自")) {
                                int i = 3 + response.indexOf("来自：");
                                int j = response.indexOf("</center>");
                                strIntentAddress = response
                                        .substring(i, j);
                                Log.e(TAG, strIntentAddress);
//                                Toast.makeText(TestActivity.this,strIntentAddress,Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求错误:" + error.toString());
            }
        });
        // 3 将StringRequest添加到RequestQueue
        requestQueue.add(mStringRequest);
    }

}
