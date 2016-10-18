package com.wfx.autorunner.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.wfx.autorunner.controller.ScriptRunning;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016-10-17.
 */
public class VpnSetting {
    public final static String TAG = "VpnSetting";
    static public void openVPNSetting(Context context) {
        Intent intent = new Intent("android.net.vpn.SETTINGS");
        context.startActivity(intent);

    }

    static public void openVPN(){
        ScriptRunning scr = new ScriptRunning();
        if (!isVpnUsed()) {
            scr.openVPN();
        }
    }

    static public void closeVPN() {
        ScriptRunning scr = new ScriptRunning();
        if (isVpnUsed()) {
            scr.closeVPN();
        }
    }

    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d(TAG, "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

}
