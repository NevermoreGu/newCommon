package com.batman.utils;

import android.app.Application;
import android.support.v4.content.FileProvider;

public class UtilsFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        //noinspection ConstantConditions
        Utils.init((Application) getContext().getApplicationContext());
        return true;
    }
}
