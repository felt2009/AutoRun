package com.wfx.autorunner.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wfx.autorunner.BuildConfig;

import org.json.JSONObject;

/**
 * Created by sean on 9/27/16.
 */
public class ServerApiManager {
    private final static String TAG = "ServerApiManager";
    private ServerApiManager() {}
    private static ServerApiManager sInstance;
    private Context context;
    private RequestQueue requestQueue;
    public enum ScriptType {
        TYPE_RETENTION(0),
        TYPE_ACTIVATE(1),
        TYPE_ALL(2);
        private final int value;
        ScriptType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public interface Listener {
        void onStart();
        void onFinished(boolean success, JSONObject response);
    }
    public static ServerApiManager instance() {
        if (sInstance == null) {
            sInstance = new ServerApiManager();
        }
        return sInstance;
    }
    public void init(Context context) {
        this.context = context.getApplicationContext();
        requestQueue =  Volley.newRequestQueue(context);
    }
    public void fetchEnvironmentInfo(final Listener listener, String packageName, String area,
                                     String tag) {
        JSONObject jsonRequest = new JSONObject();
        String api = BuildConfig.SEVER_DOMAIN + BuildConfig.INSTALL_ACTION_API +
                "?packagename=" + packageName + "&area=" + area;
        Log.d(TAG, "fetchEnvironmentInfo() api:" + api);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(api, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        if (listener != null) {
                            listener.onFinished(true, jsonObject);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null ) {
                            Log.d(TAG, "fetchEnvironmentInfo() error:" + error);
                        }
                        if (listener != null) {
                            listener.onFinished(false, null);
                        }
                    }
                });
        if (tag != null) {
            jsonObjectRequest.setTag(tag);
        }
        requestQueue.add(jsonObjectRequest);
        if (listener != null) {
            listener.onStart();
        }
    }


    public void fetchRetentionInfo(final Listener listener, String packageName, String area,
                                     String tag) {
        JSONObject jsonRequest = new JSONObject();
        String api = BuildConfig.SEVER_DOMAIN + BuildConfig.OPEN_ACTION_API +
                "?packagename=" + packageName + "&area=" + area;
        Log.d(TAG, "fetchRetentionInfo() api:" + api);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(api, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        if (listener != null) {
                            listener.onFinished(true, jsonObject);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null ) {
                            Log.d(TAG, "fetchRetentionInfo() error:" + error);
                        }
                        if (listener != null) {
                            listener.onFinished(false, null);
                        }
                    }
                });
        if (tag != null) {
            jsonObjectRequest.setTag(tag);
        }
        requestQueue.add(jsonObjectRequest);
        if (listener != null) {
            listener.onStart();
        }
    }

    public void fetchScriptInfo(final Listener listener, String packageName, ScriptType scriptType,
                                   String tag) {
        JSONObject jsonRequest = new JSONObject();
        String api = BuildConfig.SEVER_DOMAIN + BuildConfig.GET_SCRIPT_API +
                "?packagename=" + packageName + "&type=" + scriptType.getValue();
        Log.d(TAG, "fetchScriptInfo() api:" + api);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(api, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        if (listener != null) {
                            listener.onFinished(true, jsonObject);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null ) {
                            Log.d(TAG, "fetchScriptInfo() error:" + error);
                        }
                        if (listener != null) {
                            listener.onFinished(false, null);
                        }
                    }
                });
        if (tag != null) {
            jsonObjectRequest.setTag(tag);
        }
        requestQueue.add(jsonObjectRequest);
        if (listener != null) {
            listener.onStart();
        }
    }

    public void cancelReq(String tag) {
        if (requestQueue != null && tag != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
