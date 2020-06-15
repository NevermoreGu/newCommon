package com.batman.ui.skin.handler;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.util.UIDrawableHelper;


public class UISkinRuleTextCompoundTintColorHandler extends UISkinRuleColorStateListHandler {

    @Override
    protected void handle(@NonNull View view, @NonNull String name, ColorStateList colorStateList) {
        if(colorStateList == null){
            return;
        }
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setCompoundDrawableTintList(colorStateList);
            } else if (tv instanceof TintableCompoundDrawablesView) {
                ((TintableCompoundDrawablesView) tv).setSupportCompoundDrawablesTintList(colorStateList);
            } else {
                Drawable[] drawables = tv.getCompoundDrawables();
                for (int i = 0; i < drawables.length; i++) {
                    Drawable drawable = drawables[i];
                    if (drawable != null) {
                        drawable = drawable.mutate();
                        UIDrawableHelper.setDrawableTintColor(drawable, colorStateList.getDefaultColor());
                        drawables[i] = drawable;
                    }
                }
                tv.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }else{
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
