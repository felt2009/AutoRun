package com.wfx.autorunner.xposed;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by 28147 on 2016/8/4.
 */

public class MyStringRequest extends StringRequest {

    //public  MyStringRequest()
    public MyStringRequest(String url, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    /**
     * 重写以解决乱码问题
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String str = null;
        try {
            str = new String(response.data, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(str,
                HttpHeaderParser.parseCacheHeaders(response));
    }
}