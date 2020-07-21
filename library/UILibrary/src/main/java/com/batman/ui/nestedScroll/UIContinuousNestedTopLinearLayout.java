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

package com.batman.ui.nestedScroll;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.AttributeSet;

import com.batman.ui.layout.UILinearLayout;

public class UIContinuousNestedTopLinearLayout extends UILinearLayout implements IUIContinuousNestedTopView {


    public UIContinuousNestedTopLinearLayout(Context context) {
        super(context);
    }

    public UIContinuousNestedTopLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIContinuousNestedTopLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public int consumeScroll(int dyUnconsumed) {
        return dyUnconsumed;
    }

    @Override
    public int getCurrentScroll() {
        return 0;
    }

    @Override
    public int getScrollOffsetRange() {
        return 0;
    }

    @Override
    public void injectScrollNotifier(OnScrollNotifier notifier) {

    }

    @Override
    public void restoreScrollInfo(@NonNull Bundle bundle) {

    }

    @Override
    public void saveScrollInfo(@NonNull Bundle bundle) {

    }
}
