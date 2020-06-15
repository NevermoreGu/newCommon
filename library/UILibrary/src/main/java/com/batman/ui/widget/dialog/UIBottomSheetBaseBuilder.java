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
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.batman.ui.R;
import com.batman.ui.layout.UIButton;
import com.batman.ui.layout.UIPriorityLinearLayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinManager;
import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.util.UIResHelper;
import com.batman.ui.widget.textview.UISpanTouchFixTextView;


public abstract class UIBottomSheetBaseBuilder<T extends UIBottomSheetBaseBuilder> {
    private Context mContext;
    protected UIBottomSheet mDialog;
    private CharSequence mTitle;
    private boolean mAddCancelBtn;
    private String mCancelText;
    private DialogInterface.OnDismissListener mOnBottomDialogDismissListener;
    private int mRadius = -1;
    private boolean mAllowDrag = false;
    private UISkinManager mSkinManager;
    private UIBottomSheetBehavior.DownDragDecisionMaker mDownDragDecisionMaker = null;

    public UIBottomSheetBaseBuilder(Context context) {
        mContext = context;
    }

    @SuppressWarnings("unchecked")
    public T setTitle(CharSequence title) {
        mTitle = title;
        return (T) this;
    }

    protected boolean hasTitle() {
        return mTitle != null && mTitle.length() != 0;
    }

    @SuppressWarnings("unchecked")
    public T setAllowDrag(boolean allowDrag) {
        mAllowDrag = allowDrag;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setSkinManager(@Nullable UISkinManager skinManager) {
        mSkinManager = skinManager;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setDownDragDecisionMaker(UIBottomSheetBehavior.DownDragDecisionMaker downDragDecisionMaker) {
        mDownDragDecisionMaker = downDragDecisionMaker;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setAddCancelBtn(boolean addCancelBtn) {
        mAddCancelBtn = addCancelBtn;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCancelText(String cancelText) {
        mCancelText = cancelText;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setRadius(int radius) {
        mRadius = radius;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnBottomDialogDismissListener(DialogInterface.OnDismissListener listener) {
        mOnBottomDialogDismissListener = listener;
        return (T) this;
    }

    public UIBottomSheet build() {
        return build(R.style.UI_BottomSheet);
    }

    public UIBottomSheet build(int style) {
        mDialog = new UIBottomSheet(mContext, style);
        Context dialogContext = mDialog.getContext();
        UIBottomSheetRootLayout rootLayout = mDialog.getRootView();
        rootLayout.removeAllViews();
        View titleView = onCreateTitleView(mDialog, rootLayout, dialogContext);
        if (titleView != null) {
            mDialog.addContentView(titleView);
        }
        onAddCustomViewBetweenTitleAndContent(mDialog, rootLayout, dialogContext);
        View contentView = onCreateContentView(mDialog, rootLayout, dialogContext);
        if (contentView != null) {
            UIPriorityLinearLayout.LayoutParams lp = new UIPriorityLinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setPriority(UIPriorityLinearLayout.LayoutParams.PRIORITY_DISPOSABLE);
            mDialog.addContentView(contentView, lp);
        }
        onAddCustomViewAfterContent(mDialog, rootLayout, dialogContext);

        if (mAddCancelBtn) {
            mDialog.addContentView(onCreateCancelBtn(mDialog, rootLayout, dialogContext),
                    new UIPriorityLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            UIResHelper.getAttrDimen(dialogContext,
                                    R.attr.ui_bottom_sheet_cancel_btn_height)));
        }

        if (mOnBottomDialogDismissListener != null) {
            mDialog.setOnDismissListener(mOnBottomDialogDismissListener);
        }
        if (mRadius != -1) {
            mDialog.setRadius(mRadius);
        }
        mDialog.setSkinManager(mSkinManager);
        UIBottomSheetBehavior behavior = mDialog.getBehavior();
        behavior.setAllowDrag(mAllowDrag);
        behavior.setDownDragDecisionMaker(mDownDragDecisionMaker);
        return mDialog;
    }


    @Nullable
    protected View onCreateTitleView(@NonNull UIBottomSheet bottomSheet,
                                     @NonNull UIBottomSheetRootLayout rootLayout,
                                     @NonNull Context context) {
        if (hasTitle()) {
            UISpanTouchFixTextView tv = new UISpanTouchFixTextView(context);
            tv.setId(R.id.ui_bottom_sheet_title);
            tv.setText(mTitle);
            tv.onlyShowBottomDivider(0, 0, 1,
                    UIResHelper.getAttrColor(context, R.attr.ui_skin_support_bottom_sheet_separator_color));
            UIResHelper.assignTextViewWithAttr(tv, R.attr.ui_bottom_sheet_title_style);
            UISkinValueBuilder valueBuilder = UISkinValueBuilder.acquire();

            valueBuilder.textColor(R.attr.ui_skin_support_bottom_sheet_title_text_color);
            valueBuilder.bottomSeparator(R.attr.ui_skin_support_bottom_sheet_separator_color);
            UISkinHelper.setSkinValue(tv, valueBuilder);
            valueBuilder.release();
            return tv;
        }
        return null;
    }

    protected void onAddCustomViewBetweenTitleAndContent(@NonNull UIBottomSheet bottomSheet,
                                                         @NonNull UIBottomSheetRootLayout rootLayout,
                                                         @NonNull Context context) {
    }

    @Nullable
    protected abstract View onCreateContentView(@NonNull UIBottomSheet bottomSheet,
                                                @NonNull UIBottomSheetRootLayout rootLayout,
                                                @NonNull Context context);

    protected void onAddCustomViewAfterContent(@NonNull UIBottomSheet bottomSheet,
                                               @NonNull UIBottomSheetRootLayout rootLayout,
                                               @NonNull Context context) {
    }

    @NonNull
    protected View onCreateCancelBtn(@NonNull final UIBottomSheet bottomSheet,
                                     @NonNull UIBottomSheetRootLayout rootLayout,
                                     @NonNull Context context) {
        UIButton button = new UIButton(context);
        button.setId(R.id.ui_bottom_sheet_cancel);
        if (mCancelText == null || mCancelText.isEmpty()) {
            mCancelText = context.getString(R.string.ui_cancel);
        }
        button.setPadding(0, 0,0, 0);
        button.setBackground(UIResHelper.getAttrDrawable(
                context, R.attr.ui_skin_support_bottom_sheet_cancel_bg));
        button.setText(mCancelText);
        UIResHelper.assignTextViewWithAttr(button, R.attr.ui_bottom_sheet_cancel_style);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.cancel();
            }
        });
        button.onlyShowTopDivider(0, 0, 1,
                UIResHelper.getAttrColor(
                        context, R.attr.ui_skin_support_bottom_sheet_separator_color));

        UISkinValueBuilder valueBuilder = UISkinValueBuilder.acquire();
        valueBuilder.textColor(R.attr.ui_skin_support_bottom_sheet_cancel_text_color);
        valueBuilder.topSeparator(R.attr.ui_skin_support_bottom_sheet_separator_color);
        valueBuilder.background(R.attr.ui_skin_support_bottom_sheet_cancel_bg);
        UISkinHelper.setSkinValue(button, valueBuilder);
        valueBuilder.release();
        return button;
    }
}
