package com.batman.ui.widget.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.batman.ui.util.UILangHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class UIBridgeWebViewClient extends UIWebViewClient {
    public static final String UI_BRIDGE_HAS_MESSAGE = "ui://__QUEUE_MESSAGE__";
    public static final String UI_BRIDGE_JS = "UIWebviewBridge.js";

    private UIWebViewBridgeHandler mWebViewBridgeHandler;

    public UIBridgeWebViewClient(boolean needDispatchSafeAreaInset,
                                   boolean disableVideoFullscreenBtnAlways,
                                   @NonNull UIWebViewBridgeHandler bridgeHandler) {
        super(needDispatchSafeAreaInset, disableVideoFullscreenBtnAlways);
        mWebViewBridgeHandler = bridgeHandler;
    }

    @Override
    public final boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(UI_BRIDGE_HAS_MESSAGE)) {
            mWebViewBridgeHandler.fetchAndMessageFromJs();
            return true;
        }
        return onShouldOverrideUrlLoading(view, url);
    }

    protected boolean onShouldOverrideUrlLoading(WebView view, String url){
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public final boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            if (url.startsWith(UI_BRIDGE_HAS_MESSAGE)) {
                mWebViewBridgeHandler.fetchAndMessageFromJs();
                return true;
            }
        }

        return onShouldOverrideUrlLoading(view, request);
    }

    @TargetApi(24)
    protected boolean onShouldOverrideUrlLoading(WebView view, WebResourceRequest request){
        return super.shouldOverrideUrlLoading(view, request);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String bridgeScript = loadBridgeScript(view.getContext());
        if (bridgeScript != null) {
            view.evaluateJavascript(bridgeScript, null);
            mWebViewBridgeHandler.onBridgeLoaded();
        }
    }

    @Nullable
    private static String loadBridgeScript(Context context) {
        InputStream in = null;
        try {
            in = context.getAssets().open(UI_BRIDGE_JS);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            in.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UILangHelper.close(in);
        }
        return null;
    }
}
