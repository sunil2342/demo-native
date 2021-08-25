package com.demo_web_view;

import android.webkit.JavascriptInterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;

public class JavaScriptInterface{
    WebViewActivity activity;
    JavaScriptInterface(WebViewActivity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void emit(String listener, String listener1) {
        System.out.println("postMessage======= "+listener) ;
        activity.emit(listener,listener1);
    }
}