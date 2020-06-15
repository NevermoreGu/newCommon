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

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class UIBottomSheetGridItemModel {
    Drawable image = null;
    int imageRes = 0;
    int imageSkinTintColorAttr = 0;
    int imageSkinSrcAttr = 0;
    int textSkinColorAttr = 0;
    CharSequence text;
    Object tag = "";
    Drawable subscript = null;
    int subscriptRes = 0;
    int subscriptSkinTintColorAttr = 0;
    int subscriptSkinSrcAttr = 0;
    Typeface typeface;

    public UIBottomSheetGridItemModel(CharSequence text, Object tag) {
        this.text = text;
        this.tag = tag;
    }

    public UIBottomSheetGridItemModel image(Drawable image) {
        this.image = image;
        return this;
    }

    public UIBottomSheetGridItemModel image(int imageRes) {
        this.imageRes = imageRes;
        return this;
    }

    public UIBottomSheetGridItemModel subscript(Drawable image) {
        this.subscript = image;
        return this;
    }

    public UIBottomSheetGridItemModel subscript(int imageRes) {
        this.subscriptRes = imageRes;
        return this;
    }


    public UIBottomSheetGridItemModel skinTextColorAttr(int attr) {
        this.textSkinColorAttr = attr;
        return this;
    }


    public UIBottomSheetGridItemModel skinImageTintColorAttr(int attr) {
        this.imageSkinTintColorAttr = attr;
        return this;
    }

    public UIBottomSheetGridItemModel skinImageSrcAttr(int attr) {
        this.imageSkinSrcAttr = attr;
        return this;
    }

    public UIBottomSheetGridItemModel skinSubscriptTintColorAttr(int attr) {
        this.subscriptSkinTintColorAttr = attr;
        return this;
    }

    public UIBottomSheetGridItemModel skinSubscriptSrcAttr(int attr) {
        this.subscriptSkinSrcAttr = attr;
        return this;
    }

    public UIBottomSheetGridItemModel typeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public CharSequence getText() {
        return text;
    }

    public Drawable getImage() {
        return image;
    }

    public Drawable getSubscript() {
        return subscript;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getImageSkinSrcAttr() {
        return imageSkinSrcAttr;
    }

    public int getImageSkinTintColorAttr() {
        return imageSkinTintColorAttr;
    }

    public int getSubscriptRes() {
        return subscriptRes;
    }

    public int getSubscriptSkinSrcAttr() {
        return subscriptSkinSrcAttr;
    }

    public int getSubscriptSkinTintColorAttr() {
        return subscriptSkinTintColorAttr;
    }

    public int getTextSkinColorAttr() {
        return textSkinColorAttr;
    }

    public Object getTag() {
        return tag;
    }

    public Typeface getTypeface() {
        return typeface;
    }
}
