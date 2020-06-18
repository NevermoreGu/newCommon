package com.batman.ui.skin.handler;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.widget.UISlider;

public class UISkinRuleHintColorHandler extends UISkinRuleColorStateListHandler {
    @Override
    protected void handle(@NonNull View view, @NonNull String name, ColorStateList colorStateList) {
        if (view instanceof TextView) {
            ((TextView) view).setHintTextColor(colorStateList);
        } else if (view instanceof TextInputLayout) {
//            ((TextInputLayout) view).setHintTextColor(colorStateList);
        }else if(view instanceof UISlider){
            ((UISlider)view).setBarProgressColor(colorStateList.getDefaultColor());
        }else{
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
