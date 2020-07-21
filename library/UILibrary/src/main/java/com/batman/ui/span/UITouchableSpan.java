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

package com.batman.ui.span;

import android.content.res.Resources;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.batman.ui.UILog;
import com.batman.ui.link.ITouchableSpan;
import com.batman.ui.skin.IUISkinHandlerSpan;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinManager;
import com.batman.ui.util.UIResHelper;


/**
 * 可 Touch 的 Span，在 {@link #setPressed(boolean)} 后根据是否 pressed 来触发不同的UI状态
 * <p>
 * 提供设置 span 的文字颜色和背景颜色的功能, 在构造时传入
 * </p>
 */
public abstract class UITouchableSpan extends ClickableSpan implements ITouchableSpan, IUISkinHandlerSpan {
    private static final String TAG = "UITouchableSpan";
    private boolean mIsPressed;
    @ColorInt private int mNormalBackgroundColor;
    @ColorInt private int mPressedBackgroundColor;
    @ColorInt private int mNormalTextColor;
    @ColorInt private int mPressedTextColor;

    private int mNormalBgAttr;
    private int mPressedBgAttr;
    private int mNormalTextColorAttr;
    private int mPressedTextColorAttr;

    private boolean mIsNeedUnderline = false;

    public abstract void onSpanClick(View widget);

    @Override
    public final void onClick(View widget) {
        if (ViewCompat.isAttachedToWindow(widget)) {
            onSpanClick(widget);
        }
    }


    public UITouchableSpan(@ColorInt int normalTextColor,
                             @ColorInt int pressedTextColor,
                             @ColorInt int normalBackgroundColor,
                             @ColorInt int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mNormalBackgroundColor = normalBackgroundColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public UITouchableSpan(View initFollowSkinView,
                             int normalTextColorAttr, int pressedTextColorAttr,
                             int normalBgAttr, int pressedBgAttr) {
        mNormalBgAttr = normalBgAttr;
        mPressedBgAttr = pressedBgAttr;
        mNormalTextColorAttr = normalTextColorAttr;
        mPressedTextColorAttr = pressedTextColorAttr;
        if (normalTextColorAttr != 0) {
            mNormalTextColor = UISkinHelper.getSkinColor(initFollowSkinView, normalTextColorAttr);
        }
        if (pressedTextColorAttr != 0) {
            mPressedTextColor = UISkinHelper.getSkinColor(initFollowSkinView, pressedTextColorAttr);
        }
        if (normalBgAttr != 0) {
            mNormalBackgroundColor = UISkinHelper.getSkinColor(initFollowSkinView, normalBgAttr);
        }
        if (pressedBgAttr != 0) {
            mPressedBackgroundColor = UISkinHelper.getSkinColor(initFollowSkinView, pressedBgAttr);
        }
    }

    public int getNormalBackgroundColor() {
        return mNormalBackgroundColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        mNormalTextColor = normalTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        mPressedTextColor = pressedTextColor;
    }

    public int getNormalTextColor() {
        return mNormalTextColor;
    }

    public int getPressedBackgroundColor() {
        return mPressedBackgroundColor;
    }

    public int getPressedTextColor() {
        return mPressedTextColor;
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    public boolean isPressed() {
        return mIsPressed;
    }

    public void setIsNeedUnderline(boolean isNeedUnderline) {
        mIsNeedUnderline = isNeedUnderline;
    }

    public boolean isNeedUnderline() {
        return mIsNeedUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor
                : mNormalBackgroundColor;
        ds.setUnderlineText(mIsNeedUnderline);
    }

    @Override
    public void handle(@NonNull View view, @NonNull UISkinManager manager, int skinIndex, @NonNull Resources.Theme theme) {
        boolean noAttrExist = true;
        if (mNormalTextColorAttr != 0) {
            mNormalTextColor = UIResHelper.getAttrColor(theme, mNormalTextColorAttr);
            noAttrExist = false;
        }
        if (mPressedTextColorAttr != 0) {
            mPressedTextColor = UIResHelper.getAttrColor(theme, mPressedTextColorAttr);
            noAttrExist = false;
        }
        if (mNormalBgAttr != 0) {
            mNormalBackgroundColor = UIResHelper.getAttrColor(theme, mNormalBgAttr);
            noAttrExist = false;
        }
        if (mPressedBgAttr != 0) {
            mPressedBackgroundColor = UIResHelper.getAttrColor(theme, mPressedBgAttr);
            noAttrExist = false;
        }

        if (noAttrExist) {
            UILog.w(TAG, "There are no attrs for skin. Please use constructor with 5 parameters");
        }
    }
}
