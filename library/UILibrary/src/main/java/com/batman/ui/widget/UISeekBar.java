package com.batman.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import android.util.AttributeSet;

import com.batman.ui.skin.UISkinValueBuilder;
import com.batman.ui.util.UIDisplayHelper;
import com.batman.ui.R;

public class UISeekBar extends UISlider {
    private int mTickHeight;
    private int mTickWidth;

    private static SimpleArrayMap<String, Integer> sDefaultSkinAttrs;

    static {
        sDefaultSkinAttrs = new SimpleArrayMap<>(2);
        sDefaultSkinAttrs.put(UISkinValueBuilder.BACKGROUND, R.attr.ui_skin_support_seek_bar_color);
        sDefaultSkinAttrs.put(UISkinValueBuilder.PROGRESS_COLOR, R.attr.ui_skin_support_seek_bar_color);
    }

    public UISeekBar(@NonNull Context context) {
        this(context, null);
    }

    public UISeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.UISeekBarStyle);
    }

    public UISeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.UISeekBar, defStyleAttr, 0);
        mTickWidth = array.getDimensionPixelSize(R.styleable.UISeekBar_ui_seek_bar_tick_width,
                UIDisplayHelper.dp2px(context, 1));
        mTickHeight = array.getDimensionPixelSize(R.styleable.UISeekBar_ui_seek_bar_tick_height,
                UIDisplayHelper.dp2px(context, 4));
        array.recycle();
        setClickToChangeProgress(true);
    }

    public void setTickHeight(int tickHeight) {
        mTickHeight = tickHeight;
        invalidate();
    }

    public void setTickWidth(int tickWidth) {
        mTickWidth = tickWidth;
        invalidate();
    }

    public int getTickHeight() {
        return mTickHeight;
    }

    @Override
    protected void drawRect(Canvas canvas, RectF rect, int barHeight, Paint paint, boolean forProgress) {
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void drawTick(Canvas canvas, int currentTickCount, int totalTickCount,
                            int left, int right, float y,
                            Paint paint, int barNormalColor, int barProgressColor) {
        if (mTickHeight <= 0 || mTickWidth <= 0 || totalTickCount < 1) {
            return;
        }
        float step = ((float) (right - left - mTickWidth)) / totalTickCount;
        float t = y - mTickHeight / 2f;
        float b = y + mTickHeight / 2f;
        float l, r;
        float x = left + mTickWidth / 2f;
        for (int i = 0; i <= totalTickCount; i++) {
            l = x - mTickWidth / 2f;
            r = x + mTickWidth / 2f;
            paint.setColor(i <= currentTickCount ? barProgressColor : barNormalColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(l, t, r, b, paint);
            x += step;
        }
    }

    @Override
    public SimpleArrayMap<String, Integer> getDefaultSkinAttrs() {
        return sDefaultSkinAttrs;
    }
}
