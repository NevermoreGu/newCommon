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

import android.content.res.Resources;
import androidx.annotation.NonNull;
import android.view.View;

import com.batman.ui.skin.UISkinManager;
import com.batman.ui.util.UIResHelper;

public abstract class UISkinRuleFloatHandler implements IUISkinRuleHandler {
    @Override
    public final void handle(@NonNull UISkinManager skinManager, @NonNull View view, @NonNull Resources.Theme theme,
                             @NonNull String name, int attr) {
        handle(view, name, UIResHelper.getAttrFloatValue(theme, attr));
    }

    protected abstract void handle(@NonNull View view,
                                   @NonNull String name, float value);
}
