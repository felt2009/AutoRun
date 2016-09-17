package com.wfx.autorunner.common.https;

import android.content.Context;
import org.apache.http.HttpEntity;

/**
 * File server https connection manager
 */
public class BackgroundConnManager {

    private static BackgroundConnManager sInstance;

    /**
     * Construct
     */
    private BackgroundConnManager() {
    }

    /**
     * Obtain the connection manager instance
     * @return HttpsConnManager instance
     */
    public static BackgroundConnManager getInstance() {
        if (sInstance == null) {
            sInstance = new BackgroundConnManager();
        }
        return sInstance;
    }

    /**
     * Do sync http-post request in background
     * @param context context
     * @param url Url
     * @param payload String payload
     */
    public synchronized String doBackgroundPost(Context context, String url, String payload,
                                                String token) {
        return SyncHttpsRequest.doSyncPost(context, url, payload, token);
    }

    /**
     * Do sync http-post request in background
     * @param context context
     * @param url Url
     * @param payload String payload
     */
    public synchronized String doBackgroundPost(Context context, String url, String payload) {
        return SyncHttpsRequest.doSyncPost(context, url, payload, null);
    }

    /**
     * Do sync http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity entity
     */
    public synchronized String doBackgroundUpload(Context context, String url, HttpEntity entity,
                                                  String token) {
        return SyncHttpsRequest.doSyncUpload(context, url, entity, token);
    }

    /**
     * Do sync http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity entity
     */
    public synchronized String doBackgroundUpload(Context context, String url, HttpEntity entity) {
        return SyncHttpsRequest.doSyncUpload(context, url, entity, null);
    }

    /**
     * Do sync http-get request to download file
     * @param context context
     * @param url Url
     * @param entity account pwd filePath
     * @param dir file dir
     * @param fileName file name
     */
    public synchronized String doBackgroundDownload(Context context, String url, HttpEntity entity,
                                                    String dir, String fileName, String token) {
        return SyncHttpsRequest.doSyncDownload(context, url, entity, dir, fileName, token);
    }

    /**
     * Do sync http-get request to download file
     * @param context context
     * @param url Url
     * @param entity account pwd filePath
     * @param dir file dir
     * @param fileName file name
     */
    public synchronized String doBackgroundDownload(Context context, String url, HttpEntity entity,
                                                    String dir, String fileName) {
        return SyncHttpsRequest.doSyncDownload(context, url, entity, dir, fileName, null);
    }

    public synchronized String doSyncBackgroundDownloadGet(Context context, String url, String dir,
            String fileName, long fileSize) {
        return SyncHttpsRequest.doSyncDownloadGet(context, url, null, dir, fileName, fileSize);
    }
}
