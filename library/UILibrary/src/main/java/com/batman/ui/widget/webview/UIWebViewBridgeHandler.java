package com.batman.ui.widget.webview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public abstract class UIWebViewBridgeHandler {
    private static final String MESSAGE_JS_FETCH_SCRIPT = "UIBridge._fetchQueueFromNative()";
    private static final String MESSAGE_JS_RESPONSE_SCRIPT = "UIBridge._handleResponseFromNative($data$)";
    private static final String MESSAGE_PARAM_HOLDER = "$data$";
    private static final String MESSAGE_CALLBACK_ID = "callbackId";
    private static final String MESSAGE_DATA = "data";
    private static final String MESSAGE_RESPONSE_ID = "id";

    private List<Pair<String, ValueCallback<String>>> mStartupMessageList = new ArrayList<>();
    private WebView mWebView;

    public UIWebViewBridgeHandler(@NonNull WebView webView) {
        mWebView = webView;
    }

    public final void evaluateBridgeScript(String script, ValueCallback<String> resultCallback) {
        if (mStartupMessageList != null) {
            mStartupMessageList.add(new Pair<>(script, resultCallback));
        } else {
            mWebView.evaluateJavascript(script, resultCallback);
        }
    }

    void onBridgeLoaded() {
        if (mStartupMessageList != null) {
            for (Pair<String, ValueCallback<String>> message : mStartupMessageList) {
                mWebView.evaluateJavascript("", null);
            }
            mStartupMessageList = null;
        }
    }


    void fetchAndMessageFromJs() {
        mWebView.evaluateJavascript(MESSAGE_JS_FETCH_SCRIPT, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                String unescaped = unescape(value);
                if (unescaped != null) {
                    try {
                        JSONArray array = new JSONArray(unescaped);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject message = array.getJSONObject(i);
                            String callbackId = message.getString(MESSAGE_CALLBACK_ID);
                            JSONObject response = new JSONObject();
                            JSONObject responseData = handleMessage(message.getString(MESSAGE_DATA));
                            if (callbackId != null) {
                                response.put(MESSAGE_RESPONSE_ID, callbackId);
                                response.put(MESSAGE_DATA, responseData);
                                String script = MESSAGE_JS_RESPONSE_SCRIPT.replace(
                                        MESSAGE_PARAM_HOLDER, escape(response.toString()));
                                mWebView.evaluateJavascript(script, null);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected abstract JSONObject handleMessage(String message);

    @Nullable
    public static String unescape(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        String ret = value.substring(1, value.length() - 1)
                .replace("\\\\", "\\")
                .replace("\\\"", "\"");
        if ("null".equals(ret)) {
            return null;
        }
        return ret;
    }

    @NonNull
    public static String escape(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return "\"null\"";
        }
        String ret = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        return "\"" + ret + "\"";
    }

}