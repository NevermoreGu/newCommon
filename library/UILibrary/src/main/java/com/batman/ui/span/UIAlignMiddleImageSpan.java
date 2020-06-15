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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;
import android.view.View;

import com.batman.ui.skin.IUISkinHandlerSpan;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinManager;
import com.batman.ui.util.UIDrawableHelper;
import com.batman.ui.util.UIResHelper;

/**
 * 支持垂直居中的ImageSpan
 *
 * @author cginechen
 * @date 2016-03-17
 */
public class UIAlignMiddleImageSpan extends ImageSpan implements IUISkinHandlerSpan {

    public static final int ALIGN_MIDDLE = -100; // 不要和父类重复

    /**
     * 规定这个Span占几个字的宽度
     */
    private float mFontWidthMultiple = -1f;

    /**
     * 是否避免父类修改FontMetrics，如果为 false 则会走父类的逻辑, 会导致FontMetrics被更改
     */
    private boolean mAvoidSuperChangeFontMetrics = false;

    @SuppressWarnings("FieldCanBeLocal") private int mWidth;
    private Drawable mDrawable;
    private int mDrawableTintColorAttr;

    /**
     * @param d                 作为 span 的 Drawable
     * @param verticalAlignment 垂直对齐方式, 如果要垂直居中, 则使用 {@link #ALIGN_MIDDLE}
     */
    public UIAlignMiddleImageSpan(Drawable d, int verticalAlignment) {
        this(d, verticalAlignment, 0);
    }

    /**
     * @param d                 作为 span 的 Drawable
     * @param verticalAlignment 垂直对齐方式, 如果要垂直居中, 则使用 {@link #ALIGN_MIDDLE}
     * @param fontWidthMultiple 设置这个Span占几个中文字的宽度, 当该值 > 0 时, span 的宽度为该值*一个中文字的宽度; 当该值 <= 0 时, span 的宽度由 {@link #mAvoidSuperChangeFontMetrics} 决定
     */
    public UIAlignMiddleImageSpan(@NonNull Drawable d, int verticalAlignment, float fontWidthMultiple) {
        super(d.mutate(), verticalAlignment);
        mDrawable = getDrawable();
        if (fontWidthMultiple >= 0) {
            mFontWidthMultiple = fontWidthMultiple;
        }
    }

    public void setSkinSupportWithTintColor(View skinFollowView, int drawableTintColorAttr) {
        mDrawableTintColorAttr = drawableTintColorAttr;
        if (mDrawable != null && skinFollowView != null && drawableTintColorAttr != 0) {
            UIDrawableHelper.setDrawableTintColor(mDrawable,
                    UISkinHelper.getSkinColor(skinFollowView, drawableTintColorAttr));
            skinFollowView.invalidate();
        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (mAvoidSuperChangeFontMetrics) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            mWidth = rect.right;
        } else {
            mWidth = super.getSize(paint, text, start, end, fm);
        }
        if (mFontWidthMultiple > 0) {
            mWidth = (int) (paint.measureText("子") * mFontWidthMultiple);
        }
        return mWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        if (mVerticalAlignment == ALIGN_MIDDLE) {
            Drawable d = mDrawable;
            canvas.save();

//            // 注意如果这样实现会有问题：TextView 有 lineSpacing 时，这里 bottom 偏大，导致偏下
//            int transY = bottom - d.getBounds().bottom; // 底对齐
//            transY -= (paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2 - d.getBounds().bottom / 2; // 居中对齐
//            canvas.translate(x, transY);
//            d.draw(canvas);
//            canvas.restore();

            Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
            int fontTop = y + fontMetricsInt.top;
            int fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top;
            int iconHeight = d.getBounds().bottom - d.getBounds().top;
            int iconTop = fontTop + (fontMetricsHeight - iconHeight) / 2;
            canvas.translate(x, iconTop);
            d.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }
    }

    /**
     * 是否避免父类修改FontMetrics，如果为 false 则会走父类的逻辑, 会导致FontMetrics被更改
     */
    public void setAvoidSuperChangeFontMetrics(boolean avoidSuperChangeFontMetrics) {
        mAvoidSuperChangeFontMetrics = avoidSuperChangeFontMetrics;
    }

    @Override
    public void handle(@NonNull View view, @NonNull UISkinManager manager, int skinIndex, @NonNull Resources.Theme theme) {
        if (mDrawableTintColorAttr != 0) {
            UIDrawableHelper.setDrawableTintColor(mDrawable,
                    UIResHelper.getAttrColor(theme, mDrawableTintColorAttr));
        }
    }
}