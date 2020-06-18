package com.batman.myapplication;

import android.util.Log;

import com.batman.baselibrary.base.BaseApplication;
import com.batman.baselibrary.delegate.ApplicationDelegate;
import com.crash.ICrashCallback;
import com.crash.TombstoneManager;
import com.network.http.HttpHandler;

import java.io.File;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyApplication extends ApplicationDelegate {

    private final String TAG = "MyApplication";

    @Override
    protected String getBaseUrl() {
        return "https://api.sit.youxinchat.com:8443/";
    }

    @Override
    public int getLevel() {
        return LEVEL_APP;
    }

    @Override
    public Class[] subDelegates() {
        return new Class[]{BaseApplication.class};
    }

    @Override
    public void onCreateDelegate() {

        // callback for java crash, native crash and ANR
        ICrashCallback callback = new ICrashCallback() {
            @Override
            public void onCrash(String logPath, String emergency) {
                Log.d(TAG, "log path: " + (logPath != null ? logPath : "(null)") + ", emergency: " + (emergency != null ? emergency : "(null)"));

                if (emergency != null) {
//                    debug(logPath, emergency);

                    // Disk is exhausted, send crash report immediately.
                    sendThenDeleteCrashLog(logPath, emergency);
                } else {
                    // Add some expanded sections. Send crash report at the next time APP startup.

                    // OK
                    TombstoneManager.appendSection(logPath, "expanded_key_1", "expanded_content");
                    TombstoneManager.appendSection(logPath, "expanded_key_2", "expanded_content_row_1\nexpanded_content_row_2");

                    // Invalid. (Do NOT include multiple consecutive newline characters ("\n\n") in the content string.)
                    // TombstoneManager.appendSection(logPath, "expanded_key_3", "expanded_content_row_1\n\nexpanded_content_row_2");

//                    debug(logPath, null);
                }
            }
        };

        Log.d(TAG, "xCrash SDK init: start");

        // Initialize xCrash.
//        MyCrash.init(getApplicationContext(), new MyCrash.InitParameters()
//                .setAppVersion("1.2.3-beta456-patch789")
//                .setJavaRethrow(true)
//                .setJavaLogCountMax(10)
//                .setJavaDumpAllThreadsWhiteList(new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
//                .setJavaDumpAllThreadsCountMax(10)
//                .setJavaCallback(callback)
//                .setNativeRethrow(true)
//                .setNativeLogCountMax(10)
//                .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
//                .setNativeDumpAllThreadsCountMax(10)
//                .setNativeCallback(callback)
//                .setAnrRethrow(true)
//                .setAnrLogCountMax(10)
//                .setAnrCallback(callback)
//                .setPlaceholderCountMax(3)
//                .setPlaceholderSizeKb(512)
//                .setLogFileMaintainDelayMs(1000));

        Log.d(TAG, "xCrash SDK init: end");

        // Send all pending crash log files.
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(File file : TombstoneManager.getAllTombstones()) {
                    sendThenDeleteCrashLog(file.getAbsolutePath(), null);
                }
            }
        }).start();
    }


    private void sendThenDeleteCrashLog(String logPath, String emergency) {
        // Parse
        //Map<String, String> map = TombstoneParser.parse(logPath, emergency);
        //String crashReport = new JSONObject(map).toString();

        // Send the crash report to server-side.
        // ......

        // If the server-side receives successfully, delete the log file.
        //
        // Note: When you use the placeholder file feature,
        //       please always use this method to delete tombstone files.
        //
        //TombstoneManager.deleteTombstone(logPath);
    }

    /**
     * 动态添加请求头
     */
    @Override
    protected HttpHandler getHttpHandler() {
        return new HttpHandler() {
            @Override
            public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                return request;
//                HashMap<String, String> hashHeads = RequestHeadParam.getInstance().getBaseParameters();
//                Request.Builder builder = request.newBuilder();
//                for (Map.Entry<String, String> entry : hashHeads.entrySet()) {
//                    String name = entry.getKey();
//                    String value = entry.getValue();
//                    builder.addHeader(name, value);
//                }
//                return builder.build();
            }
        };
    }
}
