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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.batman.ui.R;
import com.batman.ui.layout.UIConstraintLayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.skin.defaultAttr.UISkinSimpleDefaultAttrProvider;
import com.batman.ui.util.UIResHelper;
import com.batman.ui.widget.textview.UISpanTouchFixTextView;


public class UIBottomSheetGridItemView extends UIConstraintLayout {

    protected AppCompatImageView mIconIv;
    protected AppCompatImageView mSubscriptIv;
    protected TextView mTitleTv;
    protected Object mModelTag;


    public UIBottomSheetGridItemView(Context context) {
        this(context, null);
    }

    public UIBottomSheetGridItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIBottomSheetGridItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChangeAlphaWhenPress(true);
        int paddingTop = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_grid_item_padding_top);
        int paddingBottom = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_grid_item_padding_bottom);
        setPadding(0, paddingTop, 0, paddingBottom);
        mIconIv = onCreateIconView(context);
        mIconIv.setId(View.generateViewId());
        mIconIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        int iconSize = UIResHelper.getAttrDimen(context, R.attr.ui_bottom_sheet_grid_item_icon_size);
        LayoutParams iconLp = new LayoutParams(iconSize, iconSize);
        iconLp.leftToLeft = LayoutParams.PARENT_ID;
        iconLp.rightToRight = LayoutParams.PARENT_ID;
        iconLp.topToTop = LayoutParams.PARENT_ID;
        addView(mIconIv, iconLp);

        mTitleTv = onCreateTitleView(context);
        mTitleTv.setId(View.generateViewId());
        UISkinSimpleDefaultAttrProvider provider = new UISkinSimpleDefaultAttrProvider();
        provider.setDefaultSkinAttr(UISkinValueBuilder.TEXT_COLOR,
                R.attr.ui_skin_support_bottom_sheet_grid_item_text_color);
        UIResHelper.assignTextViewWithAttr(mTitleTv, R.attr.ui_bottom_sheet_grid_item_text_style);
        UISkinHelper.setSkinDefaultProvider(mTitleTv, provider);

        LayoutParams titleLp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.leftToLeft = LayoutParams.PARENT_ID;
        titleLp.rightToRight = LayoutParams.PARENT_ID;
        titleLp.topToBottom = mIconIv.getId();
        titleLp.topMargin = UIResHelper.getAttrDimen(
                context, R.attr.ui_bottom_sheet_grid_item_text_margin_top);
        addView(mTitleTv, titleLp);
    }

    protected AppCompatImageView onCreateIconView(Context context) {
        return new AppCompatImageView(context);
    }

    protected TextView onCreateTitleView(Context context) {
        return new UISpanTouchFixTextView(context);
    }

    public void render(@NonNull UIBottomSheetGridItemModel model) {
        mModelTag = model.tag;
        setTag(model.tag);
        UISkinValueBuilder builder = UISkinValueBuilder.acquire();
        renderIcon(model, builder);
        builder.clear();
        renderTitle(model, builder);
        builder.clear();
        renderSubScript(model, builder);
        builder.release();
    }

    public Object getModelTag() {
        return mModelTag;
    }

    protected void renderIcon(@NonNull UIBottomSheetGridItemModel model, @NonNull UISkinValueBuilder builder) {
        if (model.imageSkinSrcAttr != 0) {
            builder.src(model.imageSkinSrcAttr);
            UISkinHelper.setSkinValue(mIconIv, builder);
            Drawable drawable = UISkinHelper.getSkinDrawable(mIconIv, model.imageSkinSrcAttr);
            mIconIv.setImageDrawable(drawable);
        } else {
            Drawable drawable = model.image;
            if (drawable == null && model.imageRes != 0) {
                drawable = ContextCompat.getDrawable(getContext(), model.imageRes);
            }
            if (drawable != null) {
                drawable.mutate();
            }
            mIconIv.setImageDrawable(drawable);
            if (model.imageSkinTintColorAttr != 0) {
                builder.tintColor(model.imageSkinTintColorAttr);
                UISkinHelper.setSkinValue(mIconIv, builder);
            } else {
                UISkinHelper.setSkinValue(mIconIv, "");
            }
        }
    }

    protected void renderTitle(@NonNull UIBottomSheetGridItemModel model, @NonNull UISkinValueBuilder builder) {
        mTitleTv.setText(model.text);
        if (model.textSkinColorAttr != 0) {
            builder.textColor(model.textSkinColorAttr);
        }
        UISkinHelper.setSkinValue(mTitleTv, builder);
        if (model.typeface != null) {
            mTitleTv.setTypeface(model.typeface);
        }
    }

    protected void renderSubScript(@NonNull UIBottomSheetGridItemModel model, @NonNull UISkinValueBuilder builder) {
        if (model.subscriptRes != 0 || model.subscript != null || model.subscriptSkinSrcAttr != 0) {
            if (mSubscriptIv == null) {
                mSubscriptIv = new AppCompatImageView(getContext());
                mSubscriptIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.rightToRight = mIconIv.getId();
                lp.topToTop = mIconIv.getId();
                addView(mSubscriptIv, lp);
            }
            mSubscriptIv.setVisibility(View.VISIBLE);
            if (model.subscriptSkinSrcAttr != 0) {
                builder.src(model.subscriptSkinSrcAttr);
                UISkinHelper.setSkinValue(mSubscriptIv, builder);
                Drawable drawable = UISkinHelper.getSkinDrawable(mSubscriptIv, model.subscriptSkinSrcAttr);
                mIconIv.setImageDrawable(drawable);
            } else {
                Drawable drawable = model.subscript;
                if (drawable == null && model.subscriptRes != 0) {
                    drawable = ContextCompat.getDrawable(getContext(), model.subscriptRes);
                }
                if (drawable != null) {
                    drawable.mutate();
                }
                mSubscriptIv.setImageDrawable(drawable);
                if (model.subscriptSkinTintColorAttr != 0) {
                    builder.tintColor(model.subscriptSkinTintColorAttr);
                    UISkinHelper.setSkinValue(mSubscriptIv, builder);
                } else {
                    UISkinHelper.setSkinValue(mSubscriptIv, "");
                }
            }
        } else if (mSubscriptIv != null) {
            mSubscriptIv.setVisibility(View.GONE);
        }
    }
}
