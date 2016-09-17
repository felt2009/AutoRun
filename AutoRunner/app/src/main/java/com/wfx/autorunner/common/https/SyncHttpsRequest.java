package com.wfx.autorunner.common.https;

import android.content.Context;

import com.wfx.autorunner.common.BinaryFileWriter;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Synchronous https request
 */
public class SyncHttpsRequest {
    private static final String TAG = "SyncHttpsRequest";

    private static final int REQUEST_TIMEOUT = 10 * 1000;

    private static final int SO_TIMEOUT = 20 * 1000;

    public static final int HTTP_SERVER_PORT = 80;

    public static final int HTTPS_SERVER_PORT = 662;

    private static DefaultHttpClient sHttpClient;

    public static String doSyncDelete(Context context, String url, String token) {
        Log.i(TAG, url);
        String responseData = null;
        try {
            if (sHttpClient == null) {
                initHttpClient();
            }
            if (sHttpClient != null) {
                HttpDelete delete = new HttpDelete(url);
                if (null != token) {
                    delete.addHeader("auth_token", token);
                }
                HttpResponse response = sHttpClient.execute(delete);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    responseData = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, responseData);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            responseData = null;
        }
        return responseData;
    }

    /**
     * Do sync-https post request, return response data
     * @param context context
     * @param url Url
     * @param payload String payload
     */
    public static String doSyncPost(Context context, String url, String payload, String token) {
        Log.i(TAG, "Https post request->" + url + ":" + payload);
        String responseData = null;
        try {

            HttpsURLConnection conn = null;
            BufferedReader reader = null;
            OutputStream out = null;
            try {
                byte[] array = payload.getBytes("UTF-8");
                URL getUrl = new URL(url);
                conn = (HttpsURLConnection) getUrl.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(REQUEST_TIMEOUT);
                conn.setReadTimeout(SO_TIMEOUT);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Content-Length", String.valueOf(array.length));
                if (null != token) {
                    conn.setRequestProperty("auth_token", token);
                }

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null,
                        new TrustManager[] { new TrustAnyTrustManager() },
                        new SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

                out = conn.getOutputStream();
                out.write(array);
                out.flush();
                out.close();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                responseData = reader.readLine();
            }
            catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            finally {
                if (null != reader) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                if (null != out) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                if (null != conn) {
                    conn.disconnect();
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            responseData = null;
        }
        Log.i(TAG, "Https post response->" + url + ":" + responseData);
        return responseData;
    }

    /**
     * @param context
     * @param url
     * @param payload
     * @param token
     * @return
     */
    public static String doSyncPut(Context context, String url, String payload, String token) {
        Log.i(TAG, "Https put request->" + url + ":" + payload);
        String responseData = null;
        try {

            HttpsURLConnection conn = null;
            BufferedReader reader = null;
            OutputStream out = null;
            try {
                byte[] array = payload.getBytes("UTF-8");
                URL getUrl = new URL(url);
                conn = (HttpsURLConnection) getUrl.openConnection();
                conn.setRequestMethod("PUT");
                conn.setConnectTimeout(REQUEST_TIMEOUT);
                conn.setReadTimeout(SO_TIMEOUT);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Content-Length", String.valueOf(array.length));
                if (null != token) {
                    conn.setRequestProperty("auth_token", token);
                }

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null,
                        new TrustManager[] { new TrustAnyTrustManager() },
                        new SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

                out = conn.getOutputStream();
                out.write(array);
                out.flush();
                out.close();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                responseData = reader.readLine();
            }
            catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            finally {
                if (null != reader) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                if (null != out) {
                    try {
                        out.close();
                    }
                    catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                if (null != conn) {
                    conn.disconnect();
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            responseData = null;
        }
        Log.i(TAG, "Https put response->" + url + ":" + responseData);
        return responseData;
    }

    /**
     * Do sync-https get request, return response data
     * @param context context
     * @param url Url
     */
    public static String doSyncGet(Context context, String url, String token) {
        String responseData = null;
        Log.i(TAG, url);
        try {
            if (sHttpClient == null) {
                initHttpClient();
            }
            if (sHttpClient != null) {
                HttpGet get = new HttpGet(url);
                if (null != token) {
                    get.addHeader("auth_token", token);
                }
                HttpResponse response = sHttpClient.execute(get);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    responseData = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "the get ret is " + responseData);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            responseData = null;
        }
        return responseData;
    }

    /**
     * Do sync-https get request, return response data
     * @param context context
     * @param url Url
     */
    public static String doSyncDownloadGet(Context context, String url, String token, String dir,
            String fileName, long fileSize) {
        byte[] responseData;
        String filePath = null;
        try {
            if (sHttpClient == null) {
                initHttpClient();
            }
            if (sHttpClient != null) {
                HttpGet get = new HttpGet(url);
                if (null != token) {
                    get.addHeader("auth_token", token);
                }
                HttpResponse response = sHttpClient.execute(get);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    responseData = EntityUtils.toByteArray(response.getEntity());
                    if (fileSize != responseData.length && 0 != fileSize) {
                        return null;
                    }
                    BinaryFileWriter writer = new BinaryFileWriter(dir, fileName);
                    writer.write(responseData);
                    writer.complete();
                    filePath = dir + fileName;
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            filePath = null;
        }
        return filePath;
    }

    /**
     * Do sync-https get request, download file <br>
     * Use "dir + fileName" as the full path, so control the separator yourself.
     * @param context context
     * @param url Url
     * @param entity account pwd filePath
     * @param dir dir
     * @param fileName file name
     * @return local path of the file
     */
    public static String doSyncDownload(Context context, String url, HttpEntity entity, String dir,
            String fileName, String token) {
        byte[] responseData;
        String filePath = null;
        try {
            if (sHttpClient == null) {
                initHttpClient();
            }
            if (sHttpClient != null) {
                HttpPost post = new HttpPost(url);
                post.setEntity(entity);
                if (null != token) {
                    post.addHeader("auth_token", token);
                }
                post.setHeader("Content-Type", "text/html;charset=UTF-8");
                Log.i(TAG, "Download start:" + url);
                HttpResponse response = sHttpClient.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    responseData = EntityUtils.toByteArray(response.getEntity());
                    BinaryFileWriter writer = new BinaryFileWriter(dir, fileName);
                    writer.write(responseData);
                    writer.complete();
                    filePath = dir + fileName;
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            filePath = null;
        }
        Log.i(TAG, "Download complete: -> " + filePath);
        return filePath;
    }

    /**
     * Do sync-https get request, return response data
     * @param context context
     * @param url Url
     */
    public static String doSyncUpload(Context context, String url, HttpEntity entity,
            String token) {
        String responseData = null;
        try {
            if (sHttpClient == null) {
                initHttpClient();
            }
            if (sHttpClient != null) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(entity);
                if (null != token) {
                    httpPost.addHeader("auth_token", token);
                }
                Log.i(TAG, "Upload start, Entity length:" + entity.getContentLength());
                HttpResponse response = sHttpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    responseData = EntityUtils.toString(response.getEntity());
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            responseData = null;
        }
        Log.i(TAG, "Upload response:" + responseData);
        return responseData;
    }

    private static void initHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); //允许所有主机的验证
            BasicHttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            ConnManagerParams.setTimeout(params, REQUEST_TIMEOUT);
            HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, 3 * SO_TIMEOUT);
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),
                    HTTP_SERVER_PORT));
            schReg.register(new Scheme("https", sf, HTTPS_SERVER_PORT));
            ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);
            sHttpClient = new DefaultHttpClient(conManager, params);
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Be used in https post
     */
    static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    /**
     * Be used in https post
     */
    static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * Be used in https get
     */
    static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore trustStore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(trustStore);

            TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {

                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}