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
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.batman.ui.R;
import com.batman.ui.util.UIResHelper;

import java.util.List;


public class UIBottomSheetGridLineLayout extends LinearLayout {

    private int maxItemCountInLines;
    private int miniItemWidth = -1;
    private List<Pair<View, LayoutParams>> mFirstLineViews;
    private List<Pair<View, LayoutParams>> mSecondLineViews;
    private int linePaddingHor;
    private int itemWidth;

    public UIBottomSheetGridLineLayout(UIBottomSheet bottomSheet,
                                         List<Pair<View, LayoutParams>> firstLineViews,
                                         List<Pair<View, LayoutParams>> secondLineViews) {
        super(bottomSheet.getContext());
        setOrientation(VERTICAL);
        setGravity(Gravity.TOP);
        int paddingTop = UIResHelper.getAttrDimen(
                bottomSheet.getContext(), R.attr.ui_bottom_sheet_grid_padding_top);
        int paddingBottom = UIResHelper.getAttrDimen(
                bottomSheet.getContext(), R.attr.ui_bottom_sheet_grid_padding_bottom);
        setPadding(0, paddingTop, 0, paddingBottom);
        mFirstLineViews = firstLineViews;
        mSecondLineViews = secondLineViews;
        maxItemCountInLines = Math.max(
                firstLineViews != null ? firstLineViews.size() : 0,
                secondLineViews != null ? secondLineViews.size() : 0);
        linePaddingHor = UIResHelper.getAttrDimen(
                bottomSheet.getContext(), R.attr.ui_bottom_sheet_padding_hor);

        boolean hasFirstLine = false;
        if (firstLineViews != null && !firstLineViews.isEmpty()) {
            hasFirstLine = true;
            HorizontalScrollView firstLine = createHorScroller(bottomSheet, firstLineViews);
            addView(firstLine, new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        if (secondLineViews != null && !secondLineViews.isEmpty()) {
            HorizontalScrollView secondLine = createHorScroller(bottomSheet, secondLineViews);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (hasFirstLine) {
                lp.topMargin = UIResHelper.getAttrDimen(
                        bottomSheet.getContext(), R.attr.ui_bottom_sheet_grid_line_vertical_space);
            }
            addView(secondLine, lp);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        itemWidth = calculateItemWidth(
                measureWidth, maxItemCountInLines, linePaddingHor, linePaddingHor);
        if (mFirstLineViews != null) {
            for (Pair<View, LayoutParams> pair : mFirstLineViews) {
                if (pair.second.width != itemWidth) {
                    pair.second.width = itemWidth;
                }
            }
        }

        if (mSecondLineViews != null) {
            for (Pair<View, LayoutParams> pair : mSecondLineViews) {
                if (pair.second.width != itemWidth) {
                    pair.second.width = itemWidth;
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected HorizontalScrollView createHorScroller(
            UIBottomSheet bottomSheet,
            List<Pair<View, LayoutParams>> itemViews) {
        Context context = bottomSheet.getContext();
        HorizontalScrollView scroller = new HorizontalScrollView(context);
        scroller.setHorizontalScrollBarEnabled(false);
        scroller.setClipToPadding(true);

        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setGravity(Gravity.CENTER_VERTICAL);
        linear.setPadding(linePaddingHor, 0, linePaddingHor, 0);
        scroller.addView(linear, new HorizontalScrollView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < itemViews.size(); i++) {
            Pair<View, LayoutParams> pair = itemViews.get(i);
            linear.addView(pair.first, pair.second);
        }

        return scroller;
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }


    private int calculateItemWidth(int width, int calculateCount, int paddingLeft, int paddingRight) {
        if (miniItemWidth == -1) {
            miniItemWidth = UIResHelper.getAttrDimen(getContext(), R.attr.ui_bottom_sheet_grid_item_mini_width);
        }

        final int parentSpacing = width - paddingLeft - paddingRight;
        int itemWidth = miniItemWidth;
        // there is no more space for the last one item. then stretch the item width
        if (calculateCount >= 3
                && parentSpacing - calculateCount * itemWidth > 0
                && parentSpacing - calculateCount * itemWidth < itemWidth) {
            int count = parentSpacing / itemWidth;
            itemWidth = parentSpacing / count;
        }
        // if there are more items. then show half of the first that is exceeded
        // to tell user that there are more.
        if (itemWidth * calculateCount > parentSpacing) {
            int count = (width - paddingLeft) / itemWidth;
            itemWidth = (int) ((width - paddingLeft) / (count + .5f));
        }
        return itemWidth;
    }
}
