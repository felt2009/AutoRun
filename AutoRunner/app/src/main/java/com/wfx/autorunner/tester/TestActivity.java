package com.wfx.autorunner.tester;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wfx.autorunner.R;
import com.wfx.autorunner.controller.PhoneInfoHelper;
import com.wfx.autorunner.xposed.MyStringRequest;
import com.wfx.autorunner.xposed.RandomData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Just for test FIXME need remove
public class TestActivity extends AppCompatActivity {

    private static final String URL_INSTALL ="http://106.75.148.113:80/v1/install";
    private static final String URL_OPEN ="http://106.75.148.113:80/v1/open";
    private EditText et_imei,et_mac,et_blue_mac,et_androidid,et_imsi,et_osv,et_dv,et_dm,et_opid,et_sw,et_sh,et_lat,et_lng;
    private String imei,mac,blue_mac,android_id,imsi,osv,dv,dm,opid,sw,sh,lat,lng;
    private SharedPreferences preferences;
    private Button save,getPhoneUtil,getIP,getOldData;
    private String strIP,strIntentAddress;
    private PhoneInfoHelper phoneInfoHelper;
    MyTester myTester;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initView();
        myTester = new MyTester();
    }

    private void initView(){
        preferences = this.getSharedPreferences("prefs",
                Context.MODE_WORLD_READABLE);
        et_imei =(EditText)findViewById(R.id.imei);
        et_mac =(EditText)findViewById(R.id.mac);
        et_blue_mac =(EditText)findViewById(R.id.blue_mac);
        et_androidid =(EditText)findViewById(R.id.androidid);
        et_imsi =(EditText) findViewById(R.id.imsi);
        et_osv =(EditText)findViewById(R.id.osv);
        et_dv=(EditText)findViewById(R.id.dv);
        et_dm=(EditText)findViewById(R.id.dm);
        et_opid=(EditText)findViewById(R.id.opid);
        et_sw=(EditText)findViewById(R.id.sw);
        et_sh=(EditText)findViewById(R.id.sh);
        et_lat=(EditText)findViewById(R.id.lat);
        et_lng=(EditText)findViewById(R.id.lng);
        save =(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
//                runShellCommands();
            }
        });
        getPhoneUtil =(Button)findViewById(R.id.getPhoneUtil);
        getPhoneUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getInstallJson();
//                myTester.testRunOpenedPhoneInfo();
//                PhoneInfoHelper.getInstance().generatePhoneInfo(PhoneInfoHelper.TYPE_INSTALL);
            }
        });
        getIP =(Button)findViewById(R.id.getIP);
        getIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getIP("http://1212.ip138.com/ic.asp");
//                ScriptRunning util = new ScriptRunning();
//                util.runTestFlow();
//                myTester.testRunScript();
//                myTester.testRunPlan();
            }
        });
        getOldData =(Button)findViewById(R.id.getOldData);
        getOldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOpenJson();
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor pre = preferences.edit();
        String str =getAllPackageStr(getApplication());
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
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

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
    // 获取手机里所有的包名 TODO
    public static String getAllPackageStr(Context context) {
        List<String> list = new ArrayList<String>();

        List<PackageInfo> packs = context.getPackageManager()
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



    private void getOpenJson(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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

                                et_imei.setText(imei);
                                et_mac.setText(mac);
                                et_blue_mac.setText(blue_mac);
                                et_androidid.setText(android_id);
                                et_imsi.setText(imsi);
                                et_opid.setText(opid);
                                et_osv.setText(osv);
                                et_dv.setText(dv);
                                et_dm.setText(dm);
                                et_sw.setText(sw);
                                et_sh.setText(sh);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("xxx", error.getMessage(), error);
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

    private void getIP(String paramString){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        // 2 创建StringRequest对象
        MyStringRequest mStringRequest = new MyStringRequest(paramString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){


                            Log.e("xxx",response);

                            if (response.contains("您的IP是")) {
                                int k = 1 + response.indexOf('[');
                                int m = response.indexOf(']');
                                TestActivity.this.strIP = response.substring(k, m);

                                Toast.makeText(TestActivity.this,strIP,Toast.LENGTH_SHORT).show();

                            }
                            if (response.contains("来自")) {
                                int i = 3 + response.indexOf("来自：");
                                int j = response.indexOf("</center>");
                                TestActivity.this.strIntentAddress = response
                                        .substring(i, j);
                                Log.e("xxx",strIntentAddress);
                                Toast.makeText(TestActivity.this,strIntentAddress,Toast.LENGTH_SHORT).show();

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
//
//    private Process process;
//    private void requestSu() {
//        try {
//            process = Runtime.getRuntime().exec("su");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void runShellCommands() {
//        Thread thread=new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                runHomeKey();
//                Log.i(TAG, "before sleep");
//                sleep(3000);
//                Log.i(TAG, "after sleep");
//                runHomeKey();
//                SwipeToNextScreen();
//                sleep(2000);
//                SwipeToNextScreen();
//                sleep(2000);
//                SwipeToNextScreen();
//                sleep(2000);
//                SwipeToNextScreen();
//                sleep(2000);
//                runScript();
//            }
//        });
//        thread.start();
//
//    }
//
//    private void runScript() {
//        tap(653,673); //应用位置
//        sleep(2000);
//        // 针对Su权限申请还需要有针对性方案，可能是延时，也可能是TAP;
//        runVolumeDownKey();
//    }
//
//    // 按下home键
//    private void runHomeKey() {
//        execShellCmd("input keyevent 3");
//    }
//
//    // 按下Volume down 键
//    private void runVolumeDownKey() {
//        execShellCmd("sendevent /dev/input/event0 1 114 1");
//        sleep(100);
//        execShellCmd("sendevent /dev/input/event0 0 0 0");
//        sleep(100);
//        execShellCmd("sendevent /dev/input/event0 1 114 0");
//        sleep(100);
//        execShellCmd("sendevent /dev/input/event0 0 0 0");
//    }
//
//    // Swipe to Next screen
//    private void SwipeToNextScreen() {
//        execShellCmd("input swipe 500 300 40 300  ");
//    }
//    // Swipe to prev screen
//    private void SwipeToPrevScreen() {
//        execShellCmd("input swipe 50 300 500 300  ");
//    }
//
//    // 点击某个点；
//    private void tap(int x, int y) {
//        execShellCmd("input tap " + x + " " + y);
//    }
//
//    private void sleep(int ms) {
//        try {
//            Thread.sleep(ms);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void execShellCmd(String cmd) {
//        requestSu();
//        OutputStream outputStream = process.getOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(
//                outputStream);
//        try {
//            dataOutputStream.writeBytes(cmd);
//            dataOutputStream.flush();
//            dataOutputStream.close();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static String TAG = "TESTScript";
//    private void runTestFlow() {
//        Thread thread=new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                downloadScripts();
//                installScripts();
//                getPhoneInfo();
//                getVPNInfo();
//                setVPN();
//                // TODO Just for test;
//                int wrong = 0;
//                int count = 30;
//                for(int i = 0 ; i < count ; i++) {
//                    Log.i(TAG, "Total time is " + count + " time is " + i + " total is " + count);
//                    if(runScript(getTestScriptPackage(), getTestPackage())) {
//                        Log.i(TAG, "Run well");
//                    } else {
//                        wrong++;
//                        i--;
//                        Log.i(TAG, "Error, need Run again");
//                        Log.i(TAG, "Total time is " + count + " " + wrong + " times not run");
//                        if(count < wrong * 3) {
//                            Log.e(TAG, "Too many wrong running, stop running");
//                            Log.e(TAG, "Total time is " + count + " " + wrong + " times not run");
//                            break;
//                        }
//                    }
//                }
//                Log.i(TAG, "Total time is " + count + " " + wrong + " times not run");
//            }
//        });
//        thread.start();
//
//    }
//
//    // 下载脚本APK和相关信息
//    private void downloadScripts() {
//        // TODO
//        Log.i(TAG, "downloadScripts" );
//    }
//
//    // 安装脚本APK
//    private void installScripts() {
//        // TODO
//        Log.i(TAG, "installScripts");
//    }
//
//    // 获取手机信息，并存储
//    private void getPhoneInfo() {
//        getInstallJson();
//    }
//
//    // 获取VPN信息并存储
//    private void getVPNInfo() {
//
//    }
//
//    // 设置VPN
//    private void setVPN() {
//        runScript("com.cedecjaididhbkemdkda.test", "com.android.settings");
//    }
//
//    // 执行脚本, 返回值 true 正常执行， false，没有执行；
//    private boolean runScript(String scriptPackage, String testPackage) {
//        killRunningPackageWithShell(testPackage);
//        sleep(1000);
//        killRunningPackageWithShell(scriptPackage);
//        sleep(2000);
//        runPackage(scriptPackage);
//        sleep(10000);
//        runVolumeDownKey();
//        sleep(3000);
//        runPackage(testPackage);
//        sleep(5000);
//        if(isAppInFront(testPackage)) {
//            Log.i(TAG, "in Front");
//        } else {
//            Log.i(TAG, "not in Front");
//            return false;
//        }
//        sleep(45000);
//        return true;
//    }
//
//    // 获取要执行的脚本包名 TODO
//    private String getTestScriptPackage() {
//        return "com.ngmlmemjmpmcnimlme.drivea";
//
//    }
//
//    // 获取测试Apk的包名 TODO
//    private String getTestPackage() {
//        return "com.handsgo.jiakao.android";
//    }
//    // 执行APK
//    private void runPackage(String packageName) {
//        Log.i(TAG, packageName);
//        PackageManager packageManager = this.getPackageManager();
//        Intent it= packageManager.getLaunchIntentForPackage(packageName);
//        if(it==null) {
//            Log.i(TAG, "APP not found!");
//            return;
//        }
//        Log.i(TAG, "IT get " + it.getComponent());
//
//        this.startActivity(it);
//    }
//
//    // 杀掉进程，
//    private void killRunningPackageWithShell(String packageName) {
//        execShellCmd("am force-stop "+ packageName);
//        sleep(500);
//        // 2次保证杀死的概率高些；
//        execShellCmd("am force-stop "+ packageName);
//    }
//
//    // 判断包是否在前台；
//    private boolean isAppInFront(String packageName) {
//        ActivityManager activityManager =(ActivityManager) getApplicationContext().getSystemService(
//                Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
//        if (appProcesses == null)
//            return false;
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.processName.equals(packageName)
//                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                return true;
//            }
//        }
//        return false;
//    }

}
