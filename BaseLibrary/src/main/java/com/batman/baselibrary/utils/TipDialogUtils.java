package com.batman.baselibrary.utils;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;

import com.ui.widget.dialog.UITipDialog;

import java.lang.ref.WeakReference;

public class TipDialogUtils {

    private static WeakReference<UITipDialog> sProgressDialogRef;

    private static int size = 0;

    private static Object object = new Object();

    public static UITipDialog showProgressDialog(Context context) {
        return showProgressDialog(context, "加载中", true, null);
    }

    public static UITipDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, message, true, null);
    }

    public static UITipDialog showProgressDialog(Context context, String message,
                                                 boolean canCancelable, OnCancelListener listener) {

        add();
        UITipDialog dialog = getDialog();

        if (dialog != null && dialog.getContext() != context) {
            // maybe existing dialog is running in a destroyed activity cotext we should recreate one
            dismissProgressDialog();
            Log.e("dialog", "there is a leaked window here,orign context: " + dialog.getContext() + " now: " + context);
            dialog = null;
        }

        if (dialog == null) {
            dialog = new UITipDialog.Builder(context)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(message)
                    .create();
            sProgressDialogRef = new WeakReference<>(dialog);
        }


//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        try {
//            Thread.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        dialog.setCancelable(canCancelable);
        dialog.setOnCancelListener(listener);
        synchronized (object) {
            if (size == 1) {
                dialog.show();
            }
        }

        return dialog;
    }

    public static void dismissProgressDialog() {
        remove();
        UITipDialog dialog = getDialog();
        if (null == dialog) {
            return;
        }
        sProgressDialogRef.clear();
        if (dialog.isShowing()) {
            try {
                synchronized (object) {
                    if (size == 0) {
                        dialog.dismiss();
                    }
                }
            } catch (Exception e) {
                // maybe we catch IllegalArgumentException here.
            }
        }

    }

//    public static void setMessage(String message) {
//        UITipDialog dialog = getDialog();
//        if (null != dialog && dialog.isShowing() && !TextUtils.isEmpty(message)) {
//            dialog.setMessage(message);
//        }
//    }

//    public static void updateLoadingMessage(String message) {
//        UITipDialog dialog = getDialog();
//        if (null != dialog && dialog.isShowing() && !TextUtils.isEmpty(message)) {
//            dialog.updateLoadingMessage(message);
//        }
//    }

    public static boolean isShowing() {
        UITipDialog dialog = getDialog();
        return (dialog != null && dialog.isShowing());
    }

    private static UITipDialog getDialog() {
        return sProgressDialogRef == null ? null : sProgressDialogRef.get();
    }


    private static void add() {

        synchronized (object) {
            size++;
        }
    }

    private static void remove() {

        synchronized (object) {
            size--;
        }
    }

    private int getSize() {

        synchronized (object) {
            return size;
        }
    }
}
