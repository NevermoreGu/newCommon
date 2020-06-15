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
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.batman.ui.R;
import com.batman.ui.layout.UILinearLayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.util.UIResHelper;

public class UITipDialogView extends UILinearLayout {

    private final int mMaxWidth;
    private final int mMiniWidth;
    private final int mMiniHeight;

    public UITipDialogView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        int radius = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_radius);
        Drawable background = UIResHelper.getAttrDrawable(context, R.attr.ui_skin_support_tip_dialog_bg);
        int paddingHor = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_padding_horizontal);
        int paddingVer = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_padding_vertical);
        setBackground(background);
        setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        setRadius(radius);
        UISkinValueBuilder builder = UISkinValueBuilder.acquire();
        builder.background(R.attr.ui_skin_support_tip_dialog_bg);
        UISkinHelper.setSkinValue(this, builder);
        builder.release();
        mMaxWidth = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_max_width);
        mMiniWidth = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_min_width);
        mMiniHeight = UIResHelper.getAttrDimen(context, R.attr.ui_tip_dialog_min_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if(widthSize > mMaxWidth){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, widthMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean needRemeasure = false;
        if(getMeasuredWidth() < mMiniWidth){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMiniWidth, MeasureSpec.EXACTLY);
            needRemeasure = true;
        }

        if(getMeasuredHeight() < mMiniHeight){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMiniHeight, MeasureSpec.EXACTLY);
            needRemeasure = true;
        }

        if(needRemeasure){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
