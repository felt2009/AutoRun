package com.wfx.autorunner.network;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.wfx.autorunner.controller.ScriptRunning;

/**
 * Created by Administrator on 2016-10-17.
 */
public class VpnSetting {
    static public void openVPNSetting(Context context) {
        Intent intent = new Intent("android.net.vpn.SETTINGS");
        context.startActivity(intent);
    }

    static public void openVPN(){
        ScriptRunning scr = new ScriptRunning();
        scr.openVPN();
    }

    static public void closeVPN() {
        ScriptRunning scr = new ScriptRunning();
        scr.closeVPN();
    }

}
