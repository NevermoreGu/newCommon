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

package com.batman.ui.widget.dialog;

import android.content.Context;
import android.util.AttributeSet;

import com.batman.ui.R;
import com.batman.ui.layout.UIPriorityLinearLayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.util.UIResHelper;

public class UIBottomSheetRootLayout extends UIPriorityLinearLayout {

    private final int mUsePercentMinHeight;
    private final float mHeightPercent;
    private final int mMaxWidth;

    public UIBottomSheetRootLayout(Context context) {
        this(context, null);
    }

    public UIBottomSheetRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setBackground(UIResHelper.getAttrDrawable(context, R.attr.ui_skin_support_bottom_sheet_bg));
        UISkinValueBuilder builder = UISkinValueBuilder.acquire();
        builder.background(R.attr.ui_skin_support_bottom_sheet_bg);
        UISkinHelper.setSkinValue(this, builder);
        builder.release();

        int radius = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_radius);
        if (radius > 0) {
            setRadius(radius, HIDE_RADIUS_SIDE_BOTTOM);
        }
        mUsePercentMinHeight = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_use_percent_min_height);
        mHeightPercent = UIResHelper.getAttrFloatValue(context, R.attr.ui_bottom_sheet_height_percent);
        mMaxWidth = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_max_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthSize > mMaxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, widthMode);
        }
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSize >= mUsePercentMinHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int) (heightSize * mHeightPercent), MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
