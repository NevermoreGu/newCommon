package com.qmuiteam.qmui.skin.handler;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.widget.QMUISlider;

public class QMUISkinRuleHintColorHandler extends QMUISkinRuleColorStateListHandler {
    @Override
    protected void handle(@NonNull View view, @NonNull String name, ColorStateList colorStateList) {
        if (view instanceof TextView) {
            ((TextView) view).setHintTextColor(colorStateList);
        } else if (view instanceof TextInputLayout) {
            ((TextInputLayout) view).setHintTextColor(colorStateList);
        }else if(view instanceof QMUISlider){
            ((QMUISlider)view).setBarProgressColor(colorStateList.getDefaultColor());
        }else{
            QMUISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
