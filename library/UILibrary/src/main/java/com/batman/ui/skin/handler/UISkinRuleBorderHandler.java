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

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.view.View;

import com.batman.ui.layout.IUILayout;
import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.widget.UIRadiusImageView;
import com.batman.ui.widget.UISlider;
import com.batman.ui.widget.roundwidget.UIRoundButton;

public class UISkinRuleBorderHandler extends UISkinRuleColorStateListHandler {

    @Override
    protected void handle(@NonNull View view, @NonNull String name, ColorStateList colorStateList) {
        if(colorStateList == null){
            return;
        }
        if (view instanceof IUILayout) {
            ((IUILayout) view).setBorderColor(colorStateList.getDefaultColor());
        } else if (view instanceof UIRadiusImageView) {
            ((UIRadiusImageView) view).setBorderColor(colorStateList.getDefaultColor());
        } else if (view instanceof UIRoundButton) {
            ((UIRoundButton) view).setStrokeColors(colorStateList);
        } else if(view instanceof UISlider.DefaultThumbView){
            ((UISlider.DefaultThumbView)view).setBorderColor(colorStateList.getDefaultColor());
        }else{
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
