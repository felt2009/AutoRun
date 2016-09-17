package com.wfx.autorunner.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by felt on 2016-09-17.
 */
public class AppTool {

    /**
     * 显式安装
     * @param fileName
     * @param context
     */
    public void installApp(String fileName, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 静默安装
     * @param appPath
     * @return 返回安装是否成功
     */
    public  boolean installSilence(String appPath) {
        String[] args = { "pm", "install", "-r", appPath };
        String result = excuteCommand(args);
        if (null == result || result.length() == 0) {
            return false;
        } else {
            return result.lastIndexOf("Success") > 0;
        }
    }

    public void openApp(File app, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(app),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public void deleteApp(String packageName, Context context) {
        Uri packageURI = Uri.parse(packageName);   // 包名
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    public boolean deleteSilence(String appPath) {
        String[] args = { "pm", "uninstall",  appPath };
        String result = excuteCommand(args);
        if (null == result || result.length() == 0) {
            return false;
        } else {
            return result.lastIndexOf("Success") > 0;
        }
    }

    private String excuteCommand(String... args) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

}
