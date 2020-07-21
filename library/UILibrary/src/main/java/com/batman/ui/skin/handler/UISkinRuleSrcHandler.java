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
package com.batman.ui.skin.handler;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.batman.ui.skin.UISkinHelper;

public class UISkinRuleSrcHandler extends UISkinRuleDrawableHandler {

    @Override
    protected void handle(@NonNull View view, @NonNull String name, Drawable drawable) {

        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else if (view instanceof CompoundButton) {
            ((CompoundButton) view).setButtonDrawable(drawable);
        } else {
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
