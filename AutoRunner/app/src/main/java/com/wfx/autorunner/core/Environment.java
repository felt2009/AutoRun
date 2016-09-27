package com.wfx.autorunner.core;

/**
 * Created by sean on 9/27/16.
 */
public class Environment {
    private long imei;
    private String mac;
    private String blue_mac;
    private String android_id;
    private String imsi;
    private String opid;
    private String osv;
    private String dv;
    private String dm;
    private int sw;
    private int sh;

    public long getImei() {
        return imei;
    }
    public void setImei(long imei) {
        this.imei = imei;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getBlue_mac() {
        return blue_mac;
    }
    public void setBlue_mac(String blue_mac) {
        this.blue_mac = blue_mac;
    }
    public String getAndroid_id() {
        return android_id;
    }
    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }
    public String getImsi() {
        return imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getOpid() {
        return opid;
    }
    public void setOpid(String opid) {
        this.opid = opid;
    }
    public String getOsv() {
        return osv;
    }
    public void setOsv(String osv) {
        this.osv = osv;
    }
    public String getDv() {
        return dv;
    }
    public void setDv(String dv) {
        this.dv = dv;
    }
    public String getDm() {
        return dm;
    }
    public void setDm(String dm) {
        this.dm = dm;
    }
    public int getSw() {
        return sw;
    }
    public void setSw(int sw) {
        this.sw = sw;
    }
    public int getSh() {
        return sh;
    }
    public void setSh(int sh) {
        this.sh = sh;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("imei=").append(imei).append(", mac=").append(mac).append(", blue_mac=").append(blue_mac).
                append(", android_id=").append(android_id).append(", imsi=").append(imsi).append(", osv=").append(osv).
                append(", dv=").append(dv).append(", dm=").append(dm).append(", opid=").append(opid).
                append(", sw=").append(sw).append(", sh=").append(sh);
        return builder.toString();
    }
}
