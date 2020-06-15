/*
 * Tencent is pleased to support the open source community by making UI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.batman.ui.widget.popup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.batman.ui.skin.UISkinManager;
import com.batman.ui.util.UIResHelper;

import java.lang.ref.WeakReference;

public abstract class UIBasePopup<T extends UIBasePopup> {
    public static final float DIM_AMOUNT_NOT_EXIST = -1f;
    public static final int NOT_SET = -1;

    protected final PopupWindow mWindow;
    protected WindowManager mWindowManager;
    protected Context mContext;
    protected WeakReference<View> mAttachedViewRf;
    private float mDimAmount = DIM_AMOUNT_NOT_EXIST;
    private int mDimAmountAttr = 0;
    private PopupWindow.OnDismissListener mDismissListener;
    private boolean mDismissIfOutsideTouch = true;
    private UISkinManager mSkinManager;
    private UISkinManager.OnSkinChangeListener mOnSkinChangeListener = new UISkinManager.OnSkinChangeListener() {
        @Override
        public void onSkinChange(UISkinManager skinManager, int oldSkin, int newSkin) {
            if (mDimAmountAttr != 0) {
                Resources.Theme theme = skinManager.getTheme(newSkin);
                mDimAmount = UIResHelper.getAttrFloatValue(theme, mDimAmountAttr);
                updateDimAmount(mDimAmount);
                UIBasePopup.this.onSkinChange(oldSkin, newSkin);
            }
        }
    };

    private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            dismiss();
        }
    };
    private View.OnTouchListener mOutsideTouchDismissListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                mWindow.dismiss();
                return true;
            }
            return false;
        }
    };


    public UIBasePopup(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindow = new PopupWindow(context);
        initWindow();
    }

    private void initWindow() {
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setFocusable(true);
        mWindow.setTouchable(true);
        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIBasePopup.this.onDismiss();
                if (mDismissListener != null) {
                    mDismissListener.onDismiss();
                }
            }
        });
        dismissIfOutsideTouch(mDismissIfOutsideTouch);
    }

    protected void onSkinChange(int oldSkin, int newSkin){

    }

    public UISkinManager getSkinManager() {
        return mSkinManager;
    }

    public T dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    public T dimAmountAttr(int dimAmountAttr) {
        mDimAmountAttr = dimAmountAttr;
        return (T) this;
    }

    public T skinManager(@Nullable UISkinManager skinManager) {
        mSkinManager = skinManager;
        return (T) this;
    }

    public T dismissIfOutsideTouch(boolean dismissIfOutsideTouch) {
        mDismissIfOutsideTouch = dismissIfOutsideTouch;
        mWindow.setOutsideTouchable(dismissIfOutsideTouch);
        if (dismissIfOutsideTouch) {
            mWindow.setTouchInterceptor(mOutsideTouchDismissListener);
        } else {
            mWindow.setTouchInterceptor(null);
        }
        return (T) this;
    }

    public T onDismiss(PopupWindow.OnDismissListener listener) {
        mDismissListener = listener;
        return (T) this;
    }

    private void removeOldAttachStateChangeListener() {
        if (mAttachedViewRf != null) {
            View oldAttachedView = mAttachedViewRf.get();
            if (oldAttachedView != null) {
                oldAttachedView.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }
        }
    }

    public View getDecorView() {
        View decorView = null;
        try {
            if (mWindow.getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mWindow.getContentView().getParent();
                } else {
                    decorView = mWindow.getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mWindow.getContentView().getParent().getParent();
                } else {
                    decorView = (View) mWindow.getContentView().getParent();
                }
            }
        } catch (Exception ignore) {

        }

        return decorView;
    }

    protected void showAtLocation(@NonNull View parent, int x, int y) {
        if (!ViewCompat.isAttachedToWindow(parent)) {
            return;
        }
        removeOldAttachStateChangeListener();
        parent.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        mAttachedViewRf = new WeakReference<>(parent);
        mWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
        if (mSkinManager != null) {
            mSkinManager.register(mWindow);
            mSkinManager.addSkinChangeListener(mOnSkinChangeListener);
            if (mDimAmountAttr != 0) {
                Resources.Theme currentTheme = mSkinManager.getCurrentTheme();
                currentTheme = currentTheme == null ? parent.getContext().getTheme() : currentTheme;
                mDimAmount = UIResHelper.getAttrFloatValue(currentTheme, mDimAmountAttr);
            }
        }
        if (mDimAmount != DIM_AMOUNT_NOT_EXIST) {
            updateDimAmount(mDimAmount);
        }
    }

    private void updateDimAmount(float dimAmount) {
        View decorView = getDecorView();
        if (decorView != null) {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) decorView.getLayoutParams();
            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            modifyWindowLayoutParams(p);
            mWindowManager.updateViewLayout(decorView, p);
        }
    }

    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {

    }

    protected void onDismiss() {

    }

    public final void dismiss() {
        removeOldAttachStateChangeListener();
        mAttachedViewRf = null;
        if(mSkinManager != null){
            mSkinManager.unRegister(mWindow);
            mSkinManager.removeSkinChangeListener(mOnSkinChangeListener);
        }
        mWindow.dismiss();
    }
}
