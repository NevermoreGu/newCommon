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

package com.batman.ui.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.batman.ui.R;
import com.batman.ui.UILog;
import com.batman.ui.skin.defaultAttr.IUISkinDefaultAttrProvider;
import com.batman.ui.util.UIResHelper;


public class UISkinHelper {

    public static UISkinValueBuilder sSkinValueBuilder = UISkinValueBuilder.acquire();

    public static Resources.Theme getSkinTheme(@NonNull View view) {
        UISkinManager.ViewSkinCurrent current = UISkinManager.getViewSkinCurrent(view);
        Resources.Theme theme;
        if (current == null || current.index < 0) {
            theme = view.getContext().getTheme();
        } else {
            theme = UISkinManager.of(current.managerName, view.getContext()).getTheme(current.index);
        }
        return theme;
    }

    public static int getSkinColor(@NonNull View view, int colorAttr) {
        return UIResHelper.getAttrColor(getSkinTheme(view), colorAttr);
    }

    public static ColorStateList getSkinColorStateList(@NonNull View view, int colorAttr) {
        return UIResHelper.getAttrColorStateList(view.getContext(), getSkinTheme(view), colorAttr);
    }

    @Nullable
    public static Drawable getSkinDrawable(@NonNull View view, int drawableAttr) {
        return UIResHelper.getAttrDrawable(view.getContext(), getSkinTheme(view), drawableAttr);
    }


    public static void setSkinValue(@NonNull View view, UISkinValueBuilder skinValueBuilder) {
        setSkinValue(view, skinValueBuilder.build());
    }

    public static void setSkinValue(@NonNull View view, String value) {
        view.setTag(R.id.ui_skin_value, value);
        refreshViewSkin(view);

    }

    @MainThread
    public static void setSkinValue(@NonNull View view, SkinWriter writer) {
        writer.write(sSkinValueBuilder);
        setSkinValue(view, sSkinValueBuilder.build());
        sSkinValueBuilder.clear();
    }

    public static void refreshRVItemDecoration(@NonNull RecyclerView view, IUISkinHandlerDecoration itemDecoration){
        UISkinManager.ViewSkinCurrent skinCurrent = UISkinManager.getViewSkinCurrent(view);
        if(skinCurrent != null){
            UISkinManager.of(skinCurrent.managerName, view.getContext()).refreshRecyclerDecoration(view, itemDecoration, skinCurrent.index);
        }
    }

    public static int getCurrentSkinIndex(@NonNull View view) {
        UISkinManager.ViewSkinCurrent viewSkinCurrent = UISkinManager.getViewSkinCurrent(view);
        if (viewSkinCurrent != null) {
            return viewSkinCurrent.index;
        }
        return UISkinManager.DEFAULT_SKIN;
    }

    public static void refreshViewSkin(@NonNull View view){
        UISkinManager.ViewSkinCurrent skinCurrent = UISkinManager.getViewSkinCurrent(view);
        if (skinCurrent != null) {
            UISkinManager.of(skinCurrent.managerName, view.getContext()).refreshTheme(view, skinCurrent.index);
        }
    }

    public static void syncViewSkin(@NonNull View view, @NonNull View sourceView){
        UISkinManager.ViewSkinCurrent source = UISkinManager.getViewSkinCurrent(sourceView);
        if (source != null) {
            UISkinManager.ViewSkinCurrent skin = UISkinManager.getViewSkinCurrent(view);
            if(!source.equals(skin)) {
                UISkinManager.of(source.managerName, view.getContext()).dispatch(view, source.index);
            }
        }
    }

    public static void setSkinDefaultProvider(@NonNull View view,
                                              IUISkinDefaultAttrProvider provider) {
        view.setTag(R.id.ui_skin_default_attr_provider, provider);
    }

    public static void warnRuleNotSupport(View view, String rule){
        UILog.w("UISkinManager",
                view.getClass().getSimpleName() + " does't support " + rule);
    }
}
