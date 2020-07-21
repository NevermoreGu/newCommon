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
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.batman.ui.R;
import com.batman.ui.layout.UIConstraintLayout;
import com.batman.ui.layout.UIFrameLayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.skin.defaultAttr.UISkinSimpleDefaultAttrProvider;
import com.batman.ui.util.UIResHelper;
import com.batman.ui.widget.textview.UISpanTouchFixTextView;

public class UIBottomSheetListItemView extends UIConstraintLayout {

    private AppCompatImageView mIconView;
    private UISpanTouchFixTextView mTextView;
    private UIFrameLayout mRedPointView;
    private AppCompatImageView mMarkView = null;
    private int mItemHeight;

    public UIBottomSheetListItemView(Context context, boolean markStyle, boolean gravityCenter) {
        super(context);
        setBackground(UIResHelper.getAttrDrawable(
                context, R.attr.ui_skin_support_bottom_sheet_list_item_bg));
        int paddingHor = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_padding_hor);
        setPadding(paddingHor, 0, paddingHor, 0);
        UISkinValueBuilder builder = UISkinValueBuilder.acquire();
        builder.background(R.attr.ui_skin_support_bottom_sheet_list_item_bg);
        UISkinHelper.setSkinValue(this, builder);
        builder.clear();

        mIconView = new AppCompatImageView(context);
        mIconView.setId(View.generateViewId());
        mIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mTextView = new UISpanTouchFixTextView(context);
        mTextView.setId(View.generateViewId());
        UISkinSimpleDefaultAttrProvider provider = new UISkinSimpleDefaultAttrProvider();
        provider.setDefaultSkinAttr(UISkinValueBuilder.TEXT_COLOR,
                R.attr.ui_skin_support_bottom_sheet_list_item_text_color);
        UIResHelper.assignTextViewWithAttr(mTextView, R.attr.ui_bottom_sheet_list_item_text_style);
        UISkinHelper.setSkinDefaultProvider(mTextView, provider);

        mRedPointView = new UIFrameLayout(context);
        mRedPointView.setId(View.generateViewId());
        mRedPointView.setBackgroundColor(UIResHelper.getAttrColor(
                context, R.attr.ui_skin_support_bottom_sheet_list_red_point_color));
        builder.background(R.attr.ui_skin_support_bottom_sheet_list_red_point_color);
        UISkinHelper.setSkinValue(mRedPointView, builder);
        builder.clear();

        if (markStyle) {
            mMarkView = new AppCompatImageView(context);
            mMarkView.setId(View.generateViewId());
            mMarkView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mMarkView.setImageDrawable(UIResHelper.getAttrDrawable(
                    context, R.attr.ui_skin_support_bottom_sheet_list_mark));
            builder.src(R.attr.ui_skin_support_bottom_sheet_list_mark);
            UISkinHelper.setSkinValue(mMarkView, builder);
        }
        builder.release();

        int iconSize = UIResHelper.getAttrDimen(
                context, R.attr.ui_bottom_sheet_list_item_icon_size);
        LayoutParams lp = new ConstraintLayout.LayoutParams(iconSize, iconSize);
        lp.leftToLeft = LayoutParams.PARENT_ID;
        lp.topToTop = LayoutParams.PARENT_ID;
        lp.rightToLeft = mTextView.getId();
        lp.bottomToBottom = LayoutParams.PARENT_ID;
        lp.horizontalChainStyle = LayoutParams.CHAIN_PACKED;
        lp.horizontalBias = gravityCenter ? 0.5f : 0f;
        addView(mIconView, lp);

        lp = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftToRight = mIconView.getId();
        lp.rightToLeft = mRedPointView.getId();
        lp.topToTop = LayoutParams.PARENT_ID;
        lp.bottomToBottom = LayoutParams.PARENT_ID;
        lp.horizontalChainStyle = LayoutParams.CHAIN_PACKED;
        lp.horizontalBias = gravityCenter ? 0.5f : 0f;
        lp.leftMargin = UIResHelper.getAttrDimen(
                context, R.attr.ui_bottom_sheet_list_item_icon_margin_right);
        lp.goneLeftMargin = 0;
        addView(mTextView, lp);

        int redPointSize = UIResHelper.getAttrDimen(
                context, R.attr.ui_bottom_sheet_list_item_red_point_size);
        lp = new ConstraintLayout.LayoutParams(redPointSize, redPointSize);
        lp.leftToRight = mTextView.getId();
        if (markStyle) {
            lp.rightToLeft = mMarkView.getId();
            lp.rightMargin = UIResHelper.getAttrDimen(
                    context, R.attr.ui_bottom_sheet_list_item_mark_margin_left);
        } else {
            lp.rightToRight = LayoutParams.PARENT_ID;
        }
        lp.topToTop = LayoutParams.PARENT_ID;
        lp.bottomToBottom = LayoutParams.PARENT_ID;
        lp.horizontalChainStyle = LayoutParams.CHAIN_PACKED;
        lp.horizontalBias = gravityCenter ? 0.5f : 0f;
        lp.leftMargin = UIResHelper.getAttrDimen(
                context, R.attr.ui_bottom_sheet_list_item_tip_point_margin_left);
        addView(mRedPointView, lp);

        if (markStyle) {
            lp = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightToRight = LayoutParams.PARENT_ID;
            lp.topToTop = LayoutParams.PARENT_ID;
            lp.bottomToBottom = LayoutParams.PARENT_ID;
            addView(mMarkView, lp);
        }

        mItemHeight = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_list_item_height);
    }

    public void render(@NonNull UIBottomSheetListItemModel itemModel, boolean isChecked) {
        UISkinValueBuilder builder = UISkinValueBuilder.acquire();
        if (itemModel.imageSkinSrcAttr != 0) {
            builder.src(itemModel.imageSkinSrcAttr);
            UISkinHelper.setSkinValue(mIconView, builder);
            mIconView.setImageDrawable(
                    UISkinHelper.getSkinDrawable(this, itemModel.imageSkinSrcAttr));
            mIconView.setVisibility(View.VISIBLE);
        } else {
            Drawable drawable = itemModel.image;
            if (drawable == null && itemModel.imageRes != 0) {
                drawable = ContextCompat.getDrawable(getContext(), itemModel.imageRes);
            }
            if (drawable != null) {
                drawable.mutate();
                mIconView.setImageDrawable(drawable);
                if (itemModel.imageSkinTintColorAttr != 0) {
                    builder.tintColor(itemModel.imageSkinTintColorAttr);
                    UISkinHelper.setSkinValue(mIconView, builder);
                } else {
                    UISkinHelper.setSkinValue(mIconView, "");
                }
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }
        builder.clear();

        mTextView.setText(itemModel.text);
        if (itemModel.typeface != null) {
            mTextView.setTypeface(itemModel.typeface);
        }
        if (itemModel.textSkinColorAttr != 0) {
            builder.textColor(itemModel.textSkinColorAttr);
            UISkinHelper.setSkinValue(mTextView, builder);
            ColorStateList color = UISkinHelper.getSkinColorStateList(mTextView, itemModel.textSkinColorAttr);
            if (color != null) {
                mTextView.setTextColor(color);
            }
        } else {
            UISkinHelper.setSkinValue(mTextView, "");
        }

        mRedPointView.setVisibility(itemModel.hasRedPoint ? View.VISIBLE : View.GONE);

        if (mMarkView != null) {
            mMarkView.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY));
    }
}
