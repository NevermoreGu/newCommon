package com.batman.ui.skin.handler;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.batman.ui.skin.UISkinHelper;
import com.batman.ui.skin.UISkinValueBuilder;

public class UISkinRuleTextCompoundSrcHandler extends UISkinRuleDrawableHandler {
    @Override
    protected void handle(@NonNull View view, @NonNull String name, Drawable drawable) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            Drawable[] drawables = tv.getCompoundDrawables();
            if (UISkinValueBuilder.TEXT_COMPOUND_LEFT_SRC.equals(name)) {
                drawables[0] = drawable;
            } else if (UISkinValueBuilder.TEXT_COMPOUND_TOP_SRC.equals(name)) {
                drawables[1] = drawable;
            } else if (UISkinValueBuilder.TEXT_COMPOUND_RIGHT_SRC.equals(name)) {
                drawables[2] = drawable;
            } else if (UISkinValueBuilder.TEXT_COMPOUND_BOTTOM_SRC.equals(name)) {
                drawables[3] = drawable;
            }
            tv.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }else{
            UISkinHelper.warnRuleNotSupport(view, name);
        }
    }
}
