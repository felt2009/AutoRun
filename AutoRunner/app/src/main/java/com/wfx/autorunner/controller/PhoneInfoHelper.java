package com.wfx.autorunner.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.core.PhoneInfo;
import com.wfx.autorunner.xposed.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 2016/9/15.
 */
public class PhoneInfoHelper {
    static String TAG = "PhoneInfoHelper";
    // installed URL 留存？
    private static final String URL_INSTALL ="http://106.75.148.113:80/v1/install";
    // open URL 新用户？
    private static final String URL_OPEN ="http://106.75.148.113:80/v1/open";

    public final static int TYPE_OPEN = 1;
    public final static int TYPE_INSTALL = 2;
    private SharedPreferences preferences;
    private String strIP,strIntentAddress;

    static private PhoneInfoHelper mInstance = null;
    PhoneInfo phoneInfo = new PhoneInfo();

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
        phoneInfo.setStatus(PhoneInfo.INVALID);
        if(type == TYPE_OPEN) {
            getPhoneInfos(URL_OPEN);
        } else if(type == TYPE_INSTALL) {
            getPhoneInfos(URL_INSTALL);
        }
    }

    // 从指定的URL地址获取设备信息；
    public void getPhoneInfos(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            phoneInfo.getPhoneInfoFromJson(response);
                            preferences = getContext().getSharedPreferences("prefs",
                                    Context.MODE_WORLD_READABLE);
                            SharedPreferences.Editor editor = preferences.edit();
                            phoneInfo.updatePreference(editor);
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
                //在这里设置需要post的参数 TODO test code
                Map<String, String> params = new HashMap<String, String>();
                params.put("packagename", "123");
                params.put("area", "123123");
                return params;
            }
        };
        requestQueue.add(stringRequest);
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
