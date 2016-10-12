package com.wfx.autorunner.core;

import android.content.SharedPreferences;
import android.util.Log;

import com.wfx.autorunner.xposed.RandomData;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joe on 2016/9/22.
 */

// TODO need remove
public class PhoneInfo {
    static final String TAG = "PhoneInfo";
    // IMEI
    private String imei;
    // Mac Address
    private String mac;
    // Bluetooth Mac Address;
    private String blue_mac;
    // Android ID
    private String android_id;
    // IMSI
    private String imsi;
    // 手机系统版本
    private String osv;
    // 设备制造商
    private String dv;
    // 设备型号
    private String dm;
    // 运营商ID，46000、46002、46007=>中国移动    46001、46006=>中国联通    46003、46005=>中国电信
    private String opid;
    // 设备屏幕逻辑分辨率宽度，单位为像素
    private String sw;
    // 设备屏幕逻辑分辨率高度，单位为像素
    private String sh;
    // 手机维度
    private String lat;
    // 手机经度
    private String lng;

    // 当存储入Preference时，是有效状态，当开始更新或者未获取手机数据时，是无效状态；
    private boolean valid = false;

    public void setValid(boolean v) {
        this.valid = v;
    }

    public boolean getValid() {
        return valid;
    }

    public boolean getPhoneInfoFromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
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
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 将数据存储入Preferece
    public void updatePreference(SharedPreferences.Editor editor) {
        //  TODO All Package String need Add???
//        String str =getAllPackageStr();
//        editor.putString("hhlpackagename", str);

        editor.putString("imei", imei);
        editor.putString("mac", mac);
        editor.putString("bluemac", blue_mac);
        editor.putString("androidid",  android_id);
        editor.putString("imsi",imsi);
        editor.putString("opid", opid);
        editor.putString("osv", osv);
        editor.putString("dv", dv);
        editor.putString("dm", dm);

        // FIXME
        int densityDpi = RandomData.getDensityDpi(sw);
        float density = RandomData.getDensity(sh);
        float xdpi = RandomData.getDpi(densityDpi);
        float ydpi = RandomData.getDpi(densityDpi);

        editor.putString("fbl_w", sw);// 分辨率-宽
        editor.putString("fbl_h", sh);// 分辨率-高
        editor.putString("density", density + "");
        editor.putString("densityDpi", densityDpi + "");
        editor.putString("scaledDensity", density + "");
        editor.putString("xdpi", xdpi + "");
        editor.putString("ydpi", ydpi + "");
        editor.putString("sw",sw);
        editor.putString("sh", sh);
        editor.apply();
        valid = true;
        Log.i(TAG,"Current PhoneInfo " + this.toString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("imei = ").append(imei).append("mac = ").append(mac).append("blue_mac = ").append(blue_mac).
                append("android_id = ").append(android_id).append("imsi = ").append(imsi).append("osv = ").append(osv).
                append("dv = ").append(dv).append("dm = ").append(dm).append("opid = ").append(opid).
                append("sw = ").append(sw).append("sh = ").append(sh).append("lat = ").append(lat).
                append("lng = ").append(lng);
        return builder.toString();
    }
}
