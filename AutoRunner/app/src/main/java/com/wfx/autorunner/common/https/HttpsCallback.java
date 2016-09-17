package com.wfx.autorunner.common.https;

/**
 * Https response callback
 */
public interface HttpsCallback
{
    /**
     * On Https-Response
     * @param payload String payload
     * @param url url
     */
    void onResponse(String payload, String url);
}