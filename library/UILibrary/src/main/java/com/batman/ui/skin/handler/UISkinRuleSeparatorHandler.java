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

import android.support.annotation.NonNull;
import android.view.View;

import com.batman.ui.layout.IUILayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;

public class UISkinRuleSeparatorHandler extends UISkinRuleColorHandler {

    @Override
    protected void handle(@NonNull View view, @NonNull String name, int color) {
        if (view instanceof IUILayout) {
            if (UISkinValueBuilder.TOP_SEPARATOR.equals(name)) {
                ((IUILayout) view).updateTopSeparatorColor(color);
            } else if (UISkinValueBuilder.BOTTOM_SEPARATOR.equals(name)) {
                ((IUILayout) view).updateBottomSeparatorColor(color);
            } else if (UISkinValueBuilder.LEFT_SEPARATOR.equals(name)) {
                ((IUILayout) view).updateLeftSeparatorColor(color);
            } else if (UISkinValueBuilder.RIGHT_SEPARATOR.equals(name)) {
                ((IUILayout) view).updateRightSeparatorColor(color);
            }
        }else{
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
