package com.crash;

import android.text.TextUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewScreenHandler {

    private int pid;
    private String processName;
    private String appId;
    private String appVersion;
    private String logDir;

    private final Date startTime = new Date();

    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private static SimpleDateFormat getDefaultFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    private static final ViewScreenHandler instance = new ViewScreenHandler();

    private ViewScreenHandler() {
    }

    static ViewScreenHandler getInstance() {
        return instance;
    }

    void initialize(int pid, String processName, String appId, String appVersion, String logDir) {
        this.pid = pid;
        this.processName = (TextUtils.isEmpty(processName) ? "unknown" : processName);
        this.appId = appId;
        this.appVersion = appVersion;
        this.logDir = logDir;
    }

    void handlerViewScreen(String content) {

        Date viewScreenTime = new Date();

        //notify the java crash
        NativeHandler.getInstance().notifyJavaCrashed();
        AnrHandler.getInstance().notifyJavaCrashed();

        //create log file
        File logFile = null;
        try {
            String logPath = String.format(Locale.US, "%s/%s_%s_%s__%s%s", logDir, Util.logPrefix, getDefaultFormat().format(startTime), appVersion, processName, Util.logViewScreenSuffix);
            logFile = FileManager.getInstance().createLogViewScreenFile(logPath);
        } catch (Exception e) {
            MyCrash.getLogger().e(Util.TAG, "ViewScreenHandler createLogFile failed", e);
        }

        //write info to log file
        if (logFile != null) {
            FileManager.getInstance().appendText(logFile.getAbsolutePath(), content);
        }

    }

}
