package com.wfx.autorunner.common.https;

import android.content.Context;

import org.apache.http.HttpEntity;

/**
 * Https connection manager
 */
public class HttpsConnManager {

    private static HttpsConnManager sInstance;

    /**
     * Construct
     */
    private HttpsConnManager() {
    }

    /**
     * Obtain the connection manager instance
     * @return HttpsConnManager instance
     */
    public static synchronized HttpsConnManager getInstance() {
        if (sInstance == null) {
            sInstance = new HttpsConnManager();
        }
        return sInstance;
    }

    /**
     * Do async http-get request
     * @param context context
     * @param url Url
     * @param callback Response callback
     */
    public synchronized void doAsyncGet(Context context, String url, HttpsCallback callback,
            String token) {
        AsyncHttpsRequest.doAsyncGet(context, url, callback, token);
    }

    /**
     * Do async http-get request
     * @param context context
     * @param url Url
     * @param callback Response callback
     */
    public synchronized void doAsyncGet(Context context, String url, HttpsCallback callback) {
        AsyncHttpsRequest.doAsyncGet(context, url, callback, null);
    }

    /**
     * Do async http-post request
     * @param context context
     * @param url Url
     * @param payload String payload
     * @param callback Response callback
     */
    public void doAsyncPost(Context context, String url, String payload, HttpsCallback callback,
            String token) {
        AsyncHttpsRequest.doAsyncPost(context, url, payload, callback, token);
    }

    /**
     * Do async http-post request
     * @param context context
     * @param url Url
     * @param payload String payload
     * @param callback Response callback
     */
    public void doAsyncPost(Context context, String url, String payload, HttpsCallback callback) {
        AsyncHttpsRequest.doAsyncPost(context, url, payload, callback, null);
    }

    /**
     * Do async http-delete request
     * @param context context
     * @param url Url
     * @param callback Response callback
     */
    public void doAsyncDelete(Context context, String url, HttpsCallback callback, String token) {
        AsyncHttpsRequest.doAsyncDelete(context, url, callback, token);
    }

    /**
     * Do async http-delete request
     * @param context context
     * @param url Url
     * @param callback Response callback
     */
    public void doAsyncDelete(Context context, String url, HttpsCallback callback) {
        AsyncHttpsRequest.doAsyncDelete(context, url, callback, null);
    }

    /**
     * Do async http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity String payload
     * @param callback Response callback
     */
    public synchronized void doAsyncUpload(Context context, String url, HttpEntity entity,
            HttpsCallback callback, String token) {
        AsyncHttpsRequest.doAsyncUpload(context, url, entity, callback, token);
    }

    /**
     * Do async http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity String payload
     * @param callback Response callback
     */
    public synchronized void doAsyncUpload(Context context, String url, HttpEntity entity,
            HttpsCallback callback) {
        AsyncHttpsRequest.doAsyncUpload(context, url, entity, callback, null);
    }

    /**
     * Do async http-get request to download file
     * @param context context
     * @param url Url
     * @param entity entity
     * @param dir file dir
     * @param fileName file name
     * @param callback Response callback
     */
    public synchronized void doAsyncDownload(Context context, String url, HttpEntity entity,
            String dir, String fileName, HttpsCallback callback, String token) {
        AsyncHttpsRequest.doAsyncDownload(context, url, entity, dir, fileName, callback, token);
    }

    /**
     * Do async http-get request to download file
     * @param context context
     * @param url Url
     * @param entity entity
     * @param dir file dir
     * @param fileName file name
     * @param callback Response callback
     */
    public synchronized void doAsyncDownload(Context context, String url, HttpEntity entity,
            String dir, String fileName, HttpsCallback callback) {
        AsyncHttpsRequest.doAsyncDownload(context, url, entity, dir, fileName, callback, null);
    }

    /**
     * Do sync http-get request
     * @param context context
     * @param url Url
     */
    public synchronized String doSyncGet(Context context, String url, String token) {
        return SyncHttpsRequest.doSyncGet(context, url, token);
    }

    /**
     * Do sync http-get request
     * @param context context
     * @param url Url
     */
    public synchronized String doSyncGet(Context context, String url) {
        return SyncHttpsRequest.doSyncGet(context, url, null);
    }

    public synchronized String doSyncDownloadGet(Context context, String url, String dir,
            String fileName, long fileSize) {
        return SyncHttpsRequest.doSyncDownloadGet(context, url, null, dir, fileName, fileSize);
    }

    /**
     * @param context
     * @param url
     * @param playload
     * @param token
     * @return
     */
    public synchronized String doSyncPut(Context context, String url, String playload,
            String token) {
        return SyncHttpsRequest.doSyncPut(context, url, playload, token);
    }

    /**
     * @param context
     * @param url
     * @param playload
     * @return
     */
    public synchronized String doSyncPut(Context context, String url, String playload) {
        return SyncHttpsRequest.doSyncPut(context, url, playload, null);
    }

    /**
     * Do sync http-post request
     * @param context context
     * @param url Url
     * @param payload String payload
     */
    public synchronized String doSyncPost(Context context, String url, String payload,
            String token) {
        return SyncHttpsRequest.doSyncPost(context, url, payload, token);
    }

    /**
     * Do sync http-post request
     * @param context context
     * @param url Url
     * @param payload String payload
     */
    public synchronized String doSyncPost(Context context, String url, String payload) {
        return SyncHttpsRequest.doSyncPost(context, url, payload, null);
    }

    /**
     * Do sync http-delete request
     * @param context context
     * @param url Url
     */
    public synchronized String doSyncDelete(Context context, String url, String token) {
        return SyncHttpsRequest.doSyncDelete(context, url, token);
    }

    /**
     * Do sync http-delete request
     * @param context context
     * @param url Url
     */
    public synchronized String doSyncDelete(Context context, String url) {
        return SyncHttpsRequest.doSyncDelete(context, url, null);
    }

    /**
     * Do sync http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity entity
     */
    public synchronized String doSyncUpload(Context context, String url, HttpEntity entity,
            String token) {
        return SyncHttpsRequest.doSyncUpload(context, url, entity, token);
    }

    /**
     * Do sync http-post request to upload file
     * @param context context
     * @param url Url
     * @param entity entity
     */
    public synchronized String doSyncUpload(Context context, String url, HttpEntity entity) {
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
    public synchronized String doSyncDownload(Context context, String url, HttpEntity entity,
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
    public synchronized String doSyncDownload(Context context, String url, HttpEntity entity,
            String dir, String fileName) {
        return SyncHttpsRequest.doSyncDownload(context, url, entity, dir, fileName, null);
    }

}
