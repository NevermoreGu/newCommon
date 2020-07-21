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

package com.batman.ui.widget;

import android.content.Context;
import androidx.collection.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.batman.ui.alpha.UIAlphaImageButton;
import com.batman.ui.layout.UIFrameLayout;
import com.batman.ui.qqface.UIQQFaceView;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.skin.defaultAttr.IUISkinDefaultAttrProvider;
import com.batman.ui.R;


/**
 * 这是一个对 {@link UITopBar} 的代理类，需要它的原因是：
 * 我们用 fitSystemWindows 实现沉浸式状态栏后，需要将 {@link UITopBar} 的背景衍生到状态栏后面，这个时候 fitSystemWindows 是通过
 * 更改 padding 实现的，而 {@link UITopBar} 是在高度固定的前提下做各种行为的，例如按钮的垂直居中，因此我们需要在外面包裹一层并消耗 padding
 * <p>
 * 这个类一般是配合 {@link UIWindowInsetLayout} 使用，并需要设置 fitSystemWindows 为 true
 * </p>
 *
 * @author cginechen
 * @date 2016-11-26
 */

public class UITopBarLayout extends UIFrameLayout implements IUISkinDefaultAttrProvider {
    private UITopBar mTopBar;
    private SimpleArrayMap<String, Integer> mDefaultSkinAttrs = new SimpleArrayMap<>(2);

    public UITopBarLayout(Context context) {
        this(context, null);
    }

    public UITopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.UITopBarStyle);
    }

    public UITopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultSkinAttrs.put(UISkinValueBuilder.BOTTOM_SEPARATOR, R.attr.ui_skin_support_topbar_separator_color);
        mDefaultSkinAttrs.put(UISkinValueBuilder.BACKGROUND, R.attr.ui_skin_support_topbar_bg);
        mTopBar = new UITopBar(context, attrs, defStyleAttr);
        mTopBar.setBackground(null);
        mTopBar.updateBottomDivider(0, 0, 0, 0);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mTopBar.getTopBarHeight());
        addView(mTopBar, lp);
    }

    public UITopBar getTopBar() {
        return mTopBar;
    }

    public void setCenterView(View view) {
        mTopBar.setCenterView(view);
    }

    public UIQQFaceView setTitle(int resId) {
        return mTopBar.setTitle(resId);
    }

    public UIQQFaceView setTitle(String title) {
        return mTopBar.setTitle(title);
    }

    public void showTitleView(boolean toShow) {
        mTopBar.showTitleView(toShow);
    }

    public UIQQFaceView setSubTitle(int resId) {
        return mTopBar.setSubTitle(resId);
    }

    public UIQQFaceView setSubTitle(String subTitle) {
        return mTopBar.setSubTitle(subTitle);
    }

    public void setTitleGravity(int gravity) {
        mTopBar.setTitleGravity(gravity);
    }

    public void addLeftView(View view, int viewId) {
        mTopBar.addLeftView(view, viewId);
    }

    public void addLeftView(View view, int viewId, RelativeLayout.LayoutParams layoutParams) {
        mTopBar.addLeftView(view, viewId, layoutParams);
    }

    public void addRightView(View view, int viewId) {
        mTopBar.addRightView(view, viewId);
    }

    public void addRightView(View view, int viewId, RelativeLayout.LayoutParams layoutParams) {
        mTopBar.addRightView(view, viewId, layoutParams);
    }

    public UIAlphaImageButton addRightImageButton(int drawableResId, int viewId) {
        return mTopBar.addRightImageButton(drawableResId, viewId);
    }

    public UIAlphaImageButton addLeftImageButton(int drawableResId, int viewId) {
        return mTopBar.addLeftImageButton(drawableResId, viewId);
    }

    public Button addLeftTextButton(int stringResId, int viewId) {
        return mTopBar.addLeftTextButton(stringResId, viewId);
    }

    public Button addLeftTextButton(String buttonText, int viewId) {
        return mTopBar.addLeftTextButton(buttonText, viewId);
    }

    public Button addRightTextButton(int stringResId, int viewId) {
        return mTopBar.addRightTextButton(stringResId, viewId);
    }

    public Button addRightTextButton(String buttonText, int viewId) {
        return mTopBar.addRightTextButton(buttonText, viewId);
    }

    public UIAlphaImageButton addLeftBackImageButton() {
        return mTopBar.addLeftBackImageButton();
    }

    public void removeAllLeftViews() {
        mTopBar.removeAllLeftViews();
    }

    public void removeAllRightViews() {
        mTopBar.removeAllRightViews();
    }

    public void removeCenterViewAndTitleView() {
        mTopBar.removeCenterViewAndTitleView();
    }

    /**
     * 设置 TopBar 背景的透明度
     *
     * @param alpha 取值范围：[0, 255]，255表示不透明
     */
    public void setBackgroundAlpha(int alpha) {
        this.getBackground().mutate().setAlpha(alpha);
    }

    /**
     * 根据当前 offset、透明度变化的初始 offset 和目标 offset，计算并设置 Topbar 的透明度
     *
     * @param currentOffset     当前 offset
     * @param alphaBeginOffset  透明度开始变化的offset，即当 currentOffset == alphaBeginOffset 时，透明度为0
     * @param alphaTargetOffset 透明度变化的目标offset，即当 currentOffset == alphaTargetOffset 时，透明度为1
     */
    public int computeAndSetBackgroundAlpha(int currentOffset, int alphaBeginOffset, int alphaTargetOffset) {
        double alpha = (float) (currentOffset - alphaBeginOffset) / (alphaTargetOffset - alphaBeginOffset);
        alpha = Math.max(0, Math.min(alpha, 1)); // from 0 to 1
        int alphaInt = (int) (alpha * 255);
        this.setBackgroundAlpha(alphaInt);
        return alphaInt;
    }

    public void setDefaultSkinAttr(String name, int attr) {
        mDefaultSkinAttrs.put(name, attr);
    }

    @Override
    public SimpleArrayMap<String, Integer> getDefaultSkinAttrs() {
        return mDefaultSkinAttrs;
    }
}
