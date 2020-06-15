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
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.batman.ui.layout.UIConstraintLayout;


/**
 * Created by cgspine on 2018/2/28.
 */

public class UIDialogView extends UIConstraintLayout {


    private OnDecorationListener mOnDecorationListener;

    public UIDialogView(Context context) {
        this(context, null);
    }

    public UIDialogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnDecorationListener(OnDecorationListener onDecorationListener) {
        mOnDecorationListener = onDecorationListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDecorationListener != null) {
            mOnDecorationListener.onDraw(canvas, this);
        }
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mOnDecorationListener != null) {
            mOnDecorationListener.onDrawOver(canvas, this);
        }
    }

    public interface OnDecorationListener {
        void onDraw(Canvas canvas, UIDialogView view);

        void onDrawOver(Canvas canvas, UIDialogView view);
    }
}
