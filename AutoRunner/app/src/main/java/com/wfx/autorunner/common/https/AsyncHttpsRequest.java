package com.wfx.autorunner.common.https;

import android.content.Context;
import android.util.Log;

import com.wfx.autorunner.common.ThreadPool;
import org.apache.http.HttpEntity;

/**
 * Asynchronous https request
 */
public class AsyncHttpsRequest {

    protected static final String TAG = "AsyncHttpsRequest";

    /**
     * Do async-https request, response by callback
     * @param context context
     * @param url Url
     * @param payload String payload
     * @param callback Response callback
     */
    public static void doAsyncPost(Context context, String url, String payload,
            HttpsCallback callback, String token) {
        ThreadPool.execute(new HttpsPostRunnable(context, url, payload, callback, token));
    }

    /**
     * Do async-https request, response by callback
     * @param context context
     * @param url Url
     * @param callback Response callback
     */
    public static void doAsyncDelete(Context context, String url,
                                   HttpsCallback callback, String token) {
        ThreadPool.execute(new HttpsDeleteRunnable(context, url, callback, token));
    }

    /**
     * Do async-https download request, response by callback <br>
     * Use "dir + fileName" as the full path, so control the separator yourself.
     * @param context context
     * @param url Url
     * @param dir file dir
     * @param fileName file name
     * @param callBack Response callback
     */
    public static void doAsyncDownload(Context context, String url, HttpEntity entity, String dir,
            String fileName, HttpsCallback callBack, String token) {
        ThreadPool.execute(new HttpsDownloadRunnable(context, url, entity, dir, fileName, callBack, token));
    }

    /**
     * Do async-https post request to upload file, response by callback
     * @param context context
     * @param url Url
     * @param entity entity
     * @param callback Response callback
     */
    public static void doAsyncUpload(Context context, String url, HttpEntity entity,
            HttpsCallback callback, String token) {
        ThreadPool.execute(new HttpsUploadRunnable(context, url, entity, callback, token));
    }

    /**
     * Do async-https request, response by callback
     * @param url Url
     * @param callback Response callback
     */
    public static void doAsyncGet(Context context, String url, HttpsCallback callback, String token) {
        ThreadPool.execute(new HttpsGetRunnable(context, url, callback, token));
    }

    /**
     * Async Https Post Runnable
     */
    static class HttpsPostRunnable implements Runnable {
        private final Context context;

        private final String url;

        private final String payload;

        private final String token;

        private final HttpsCallback callBack;

        public HttpsPostRunnable(Context context, String url, String payload, HttpsCallback callBack, String token) {
            this.context = context;
            this.payload = payload;
            this.callBack = callBack;
            this.url = url;
            this.token = token;
        }

        @Override
        public void run() {
            String responseData = HttpsConnManager.getInstance().doSyncPost(context, url, payload, token);
            try {
                if (callBack != null) {
                    callBack.onResponse(responseData, url);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Async Https Delete Runnable
     */
    static class HttpsDeleteRunnable implements Runnable {
        private final Context context;

        private final String url;

        private final String token;

        private final HttpsCallback callBack;

        public HttpsDeleteRunnable(Context context, String url, HttpsCallback callBack, String token) {
            this.context = context;
            this.callBack = callBack;
            this.url = url;
            this.token = token;
        }

        @Override
        public void run() {
            String responseData = HttpsConnManager.getInstance().doSyncDelete(context, url,  token);
            try {
                if (callBack != null) {
                    callBack.onResponse(responseData, url);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Async Https Post File Runnable
     */
    static class HttpsUploadRunnable implements Runnable {
        private final Context context;

        private final String url;

        private final HttpEntity entity;

        private final HttpsCallback callback;

        private final String token;

        public HttpsUploadRunnable(Context context, String url, HttpEntity entity,
                HttpsCallback callback, String token) {
            this.context = context;
            this.url = url;
            this.entity = entity;
            this.callback = callback;
            this.token = token;
        }

        @Override
        public void run() {
            String responseData = HttpsConnManager.getInstance().doSyncUpload(context, url, entity, token);
            try {
                if (callback != null) {
                    Log.i(AsyncHttpsRequest.TAG, "Async upload file response:" + responseData);
                    callback.onResponse(responseData, url);
                }
            }
            catch (Exception e) {
                Log.e(AsyncHttpsRequest.TAG, e.toString());
            }
        }
    }

    /**
     * Async Https Get Runnable
     */
    static class HttpsGetRunnable implements Runnable {
        private final Context context;

        private final String url;

        private final HttpsCallback callBack;

        private final String token;

        public HttpsGetRunnable(Context context, String url, HttpsCallback callBack, String token) {
            this.context = context;
            this.callBack = callBack;
            this.url = url;
            this.token = token;
        }

        @Override
        public void run() {
            String responseData = HttpsConnManager.getInstance().doSyncGet(context, url, token);
            try {
                if (callBack != null) {
                    callBack.onResponse(responseData, url);
                }
            }
            catch (Exception e) {
                Log.e(AsyncHttpsRequest.TAG, e.toString());
            }
        }

    }

    /**
     * Async Https Download Runnable
     */
    static class HttpsDownloadRunnable implements Runnable {
        private final Context context;

        private final String url;

        private final HttpEntity entity;

        private final String dir;

        private final String fileName;

        private final HttpsCallback callBack;

        private final String token;

        public HttpsDownloadRunnable(Context context, String url, HttpEntity entity, String dir,
                String fileName, HttpsCallback callBack, String token) {
            this.context = context;
            this.url = url;
            this.entity = entity;
            this.dir = dir;
            this.fileName = fileName;
            this.callBack = callBack;
            this.token = token;
        }

        @Override
        public void run() {
            String responseData = HttpsConnManager.getInstance().doSyncDownload(context,
                    url,
                    entity,
                    dir,
                    fileName,
                    token);
            try {
                if (callBack != null) {
                    callBack.onResponse(responseData, url);
                }
            }
            catch (Exception e) {
                Log.e(AsyncHttpsRequest.TAG, e.toString());
            }
        }
    }

}
