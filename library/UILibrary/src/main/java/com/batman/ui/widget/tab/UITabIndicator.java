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

package com.batman.ui.widget.tab;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.View;

import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinManager;
import com.batman.ui.util.UIResHelper;


public class UITabIndicator {

    /**
     * the height of indicator
     */
    private int mIndicatorHeight;
    /**
     * is indicator layout in top of UITabSegment?
     */
    private boolean mIndicatorTop = false;
    /**
     * use a drawable to present the indicator
     */
    private @Nullable
    Drawable mIndicatorDrawable;
    /**
     * the width of indicator changed when toggle to different tab
     */
    private boolean mIsIndicatorWidthFollowContent = true;

    /**
     * indicator rect, draw directly
     */
    private Rect mIndicatorRect = null;

    /**
     * indicator paint, draw directly
     */
    private Paint mIndicatorPaint = null;

    private int mFixedColorAttr = 0;
    private boolean mShouldReGetFixedColor = true;
    private int mFixedColor = 0;

    public UITabIndicator(int indicatorHeight, boolean indicatorTop,
                            boolean isIndicatorWidthFollowContent){
        this(indicatorHeight, indicatorTop, isIndicatorWidthFollowContent, 0);
    }

    public UITabIndicator(int indicatorHeight, boolean indicatorTop,
                            boolean isIndicatorWidthFollowContent,  int fixedColorAttr) {
        mIndicatorHeight = indicatorHeight;
        mIndicatorTop = indicatorTop;
        mIsIndicatorWidthFollowContent = isIndicatorWidthFollowContent;
        mFixedColorAttr = fixedColorAttr;
    }

    public UITabIndicator(@NonNull Drawable drawable, boolean indicatorTop,
                            boolean isIndicatorWidthFollowContent){
        this(drawable, indicatorTop, isIndicatorWidthFollowContent, 0);
    }

    public UITabIndicator(@NonNull Drawable drawable, boolean indicatorTop,
                            boolean isIndicatorWidthFollowContent, int fixedColorAttr) {
        mIndicatorDrawable = drawable;
        mIndicatorHeight = drawable.getIntrinsicHeight();
        mIndicatorTop = indicatorTop;
        mIsIndicatorWidthFollowContent = isIndicatorWidthFollowContent;
        mFixedColorAttr = fixedColorAttr;
    }

    public boolean isIndicatorWidthFollowContent() {
        return mIsIndicatorWidthFollowContent;
    }

    public boolean isIndicatorTop() {
        return mIndicatorTop;
    }

    @Deprecated
    protected void updateInfo(int left, int width, int color){
        if (mIndicatorRect == null) {
            mIndicatorRect = new Rect(left, 0,
                    left + width, 0);
        } else {
            mIndicatorRect.left = left;
            mIndicatorRect.right = left + width;
        }
        if(mFixedColorAttr == 0){
            updateColor(color);
        }
    }

    protected void updateInfo(int left, int width, int color, float offsetPercent) {
        updateInfo(left, width, color);
    }

    protected void updateColor(int color){
        if (mIndicatorDrawable != null) {
            DrawableCompat.setTint(mIndicatorDrawable, color);
        } else {
            if (mIndicatorPaint == null) {
                mIndicatorPaint = new Paint();
                mIndicatorPaint.setStyle(Paint.Style.FILL);
            }
            mIndicatorPaint.setColor(color);
        }
    }

    protected void draw(@NonNull View hostView, @NonNull Canvas canvas, int viewTop, int viewBottom) {
        if (mIndicatorRect != null) {
            if(mFixedColorAttr != 0 && mShouldReGetFixedColor){
                mShouldReGetFixedColor = false;
                mFixedColor = UISkinHelper.getSkinColor(hostView, mFixedColorAttr);
                updateColor(mFixedColor);
            }
            if (mIndicatorTop) {
                mIndicatorRect.top = viewTop;
                mIndicatorRect.bottom = mIndicatorRect.top + mIndicatorHeight;
            } else {
                mIndicatorRect.bottom = viewBottom;
                mIndicatorRect.top = mIndicatorRect.bottom - mIndicatorHeight;
            }
            if (mIndicatorDrawable != null) {
                mIndicatorDrawable.setBounds(mIndicatorRect);
                mIndicatorDrawable.draw(canvas);
            } else {
                canvas.drawRect(mIndicatorRect, mIndicatorPaint);
            }
        }
    }

    protected void handleSkinChange(@NonNull UISkinManager manager, int skinIndex,
                                    @NonNull Resources.Theme theme,
                                    @Nullable UITab selectedTab){
        mShouldReGetFixedColor = true;
        if(selectedTab != null && mFixedColorAttr == 0){
            updateColor(
                    selectedTab.selectedColorAttr == 0 ? selectedTab.selectColor : UIResHelper.getAttrColor(theme,selectedTab.selectedColorAttr));
        }
    }
}