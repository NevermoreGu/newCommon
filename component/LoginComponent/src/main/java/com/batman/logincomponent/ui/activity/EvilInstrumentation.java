package com.batman.logincomponent.ui.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.utils.RefInvokeUtils;
import com.utils.ToastUtils;

public class EvilInstrumentation extends Instrumentation {

    Instrumentation mBase;

    public EvilInstrumentation(Instrumentation mBase) {
        this.mBase = mBase;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        ToastUtils.showLongToast(who, "哈哈哈");
        Class[] p1 = {Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class};
        Object[] v1 = {who, contextThread, token, target, intent, requestCode, options};
        return (ActivityResult) RefInvokeUtils.invokeInstanceMethod(mBase, "execStartActivity", p1, v1);
    }
}
