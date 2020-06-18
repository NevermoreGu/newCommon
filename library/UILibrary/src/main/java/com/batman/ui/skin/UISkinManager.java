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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Trace;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.batman.ui.BuildConfig;
import com.batman.ui.R;
import com.batman.ui.UILog;
import com.batman.ui.qqface.UIQQFaceView;
import com.batman.ui.skin.annotation.UISkinListenWithHierarchyChange;
import com.batman.ui.skin.defaultAttr.IUISkinDefaultAttrProvider;
import com.batman.ui.skin.handler.IUISkinRuleHandler;
import com.batman.ui.skin.handler.UISkinRuleAlphaHandler;
import com.batman.ui.skin.handler.UISkinRuleBackgroundHandler;
import com.batman.ui.skin.handler.UISkinRuleBgTintColorHandler;
import com.batman.ui.skin.handler.UISkinRuleBorderHandler;
import com.batman.ui.skin.handler.UISkinRuleHintColorHandler;
import com.batman.ui.skin.handler.UISkinRuleMoreBgColorHandler;
import com.batman.ui.skin.handler.UISkinRuleMoreTextColorHandler;
import com.batman.ui.skin.handler.UISkinRuleProgressColorHandler;
import com.batman.ui.skin.handler.UISkinRuleSeparatorHandler;
import com.batman.ui.skin.handler.UISkinRuleSrcHandler;
import com.batman.ui.skin.handler.UISkinRuleTextColorHandler;
import com.batman.ui.skin.handler.UISkinRuleTextCompoundSrcHandler;
import com.batman.ui.skin.handler.UISkinRuleTextCompoundTintColorHandler;
import com.batman.ui.skin.handler.UISkinRuleTintColorHandler;
import com.batman.ui.skin.handler.UISkinRuleUnderlineHandler;
import com.batman.ui.util.UILangHelper;
import com.batman.ui.util.UIResHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public final class UISkinManager {
    private static final String TAG = "UISkinManager";
    public static final int DEFAULT_SKIN = -1;
    private static final String[] EMPTY_ITEMS = new String[]{};
    private static ArrayMap<String, UISkinManager> sInstances = new ArrayMap<>();
    private static final String DEFAULT_NAME = "default";

    @MainThread
    public static UISkinManager defaultInstance(Context context) {
        context = context.getApplicationContext();
        return of(DEFAULT_NAME, context.getResources(), context.getPackageName());
    }

    @MainThread
    public static UISkinManager of(String name, Resources resources, String packageName) {
        UISkinManager instance = sInstances.get(name);
        if(instance == null){
            instance =  new UISkinManager(name, resources, packageName);
            sInstances.put(name, instance);
        }
        return instance;
    }

    @MainThread
    public static UISkinManager of(String name, Context context){
        context = context.getApplicationContext();
        return of(name, context.getResources(), context.getPackageName());
    }


    //==============================================================================================

    private String mName;
    private Resources mResources;
    private String mPackageName;
    private SparseArray<SkinItem> mSkins = new SparseArray<>();
    private static HashMap<String, IUISkinRuleHandler> sRuleHandlers = new HashMap<>();
    private static HashMap<Integer, Resources.Theme> sStyleIdThemeMap = new HashMap<>();

    static {
        sRuleHandlers.put(UISkinValueBuilder.BACKGROUND, new UISkinRuleBackgroundHandler());
        IUISkinRuleHandler textColorHandler = new UISkinRuleTextColorHandler();
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COLOR, textColorHandler);
        sRuleHandlers.put(UISkinValueBuilder.SECOND_TEXT_COLOR, textColorHandler);
        sRuleHandlers.put(UISkinValueBuilder.SRC, new UISkinRuleSrcHandler());
        sRuleHandlers.put(UISkinValueBuilder.BORDER, new UISkinRuleBorderHandler());
        IUISkinRuleHandler separatorHandler = new UISkinRuleSeparatorHandler();
        sRuleHandlers.put(UISkinValueBuilder.TOP_SEPARATOR, separatorHandler);
        sRuleHandlers.put(UISkinValueBuilder.RIGHT_SEPARATOR, separatorHandler);
        sRuleHandlers.put(UISkinValueBuilder.BOTTOM_SEPARATOR, separatorHandler);
        sRuleHandlers.put(UISkinValueBuilder.LEFT_SEPARATOR, separatorHandler);
        sRuleHandlers.put(UISkinValueBuilder.TINT_COLOR, new UISkinRuleTintColorHandler());
        sRuleHandlers.put(UISkinValueBuilder.ALPHA, new UISkinRuleAlphaHandler());
        sRuleHandlers.put(UISkinValueBuilder.BG_TINT_COLOR, new UISkinRuleBgTintColorHandler());
        sRuleHandlers.put(UISkinValueBuilder.PROGRESS_COLOR, new UISkinRuleProgressColorHandler());
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COMPOUND_TINT_COLOR, new UISkinRuleTextCompoundTintColorHandler());
        IUISkinRuleHandler textCompoundSrcHandler = new UISkinRuleTextCompoundSrcHandler();
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COMPOUND_LEFT_SRC, textCompoundSrcHandler);
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COMPOUND_TOP_SRC, textCompoundSrcHandler);
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COMPOUND_RIGHT_SRC, textCompoundSrcHandler);
        sRuleHandlers.put(UISkinValueBuilder.TEXT_COMPOUND_BOTTOM_SRC, textCompoundSrcHandler);
        sRuleHandlers.put(UISkinValueBuilder.HINT_COLOR, new UISkinRuleHintColorHandler());
        sRuleHandlers.put(UISkinValueBuilder.UNDERLINE, new UISkinRuleUnderlineHandler());
        sRuleHandlers.put(UISkinValueBuilder.MORE_TEXT_COLOR, new UISkinRuleMoreTextColorHandler());
        sRuleHandlers.put(UISkinValueBuilder.MORE_BG_COLOR, new UISkinRuleMoreBgColorHandler());
    }

    public static void setRuleHandler(String name, IUISkinRuleHandler handler){
        sRuleHandlers.put(name, handler);
    }

    // Actually, ViewGroup.OnHierarchyChangeListener is a better choice, but it only has a setter.
    // Add child will trigger onLayoutChange
    private static View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(
                View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (v instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) v;
                int childCount = viewGroup.getChildCount();
                if (childCount > 0) {
                    ViewSkinCurrent current = getViewSkinCurrent(viewGroup);
                    if (current != null) {
                        View child;
                        for (int i = 0; i < childCount; i++) {
                            child = viewGroup.getChildAt(i);
                            ViewSkinCurrent childTheme = getViewSkinCurrent(child);
                            if (!current.equals(childTheme)) {
                                of(current.managerName, child.getContext()).dispatch(child, current.index);
                            }
                        }
                    }
                }
            }
        }
    };


    private static ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener = new ViewGroup.OnHierarchyChangeListener() {
        @Override
        public void onChildViewAdded(View parent, View child) {
            ViewSkinCurrent current = getViewSkinCurrent(parent);
            if (current != null) {
                ViewSkinCurrent childTheme = getViewSkinCurrent(child);
                if (!current.equals(childTheme)) {
                    of(current.managerName, child.getContext()).dispatch(child, current.index);
                }
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {

        }
    };



    public UISkinManager(String name, Resources resources, String packageName) {
        mName = name;
        mResources = resources;
        mPackageName = packageName;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public Resources.Theme getTheme(int skinIndex) {
        SkinItem skinItem = mSkins.get(skinIndex);
        if (skinItem != null) {
            return skinItem.getTheme();
        }
        return null;
    }

    @Nullable
    public Resources.Theme getCurrentTheme() {
        SkinItem skinItem = mSkins.get(mCurrentSkin);
        if (skinItem != null) {
            return skinItem.getTheme();
        }
        return null;
    }

    @MainThread
    public void addSkin(int index, int styleRes) {
        if (index <= 0) {
            throw new IllegalArgumentException("index must greater than 0");
        }
        SkinItem skinItem = mSkins.get(index);
        if (skinItem != null) {
            if (skinItem.getStyleRes() == styleRes) {
                return;
            }
            throw new RuntimeException("already exist the theme item for " + index);
        }
        skinItem = new SkinItem(styleRes);
        mSkins.append(index, skinItem);
    }

    static ViewSkinCurrent getViewSkinCurrent(View view){
        Object current = view.getTag(R.id.ui_skin_current);
        if(current instanceof ViewSkinCurrent){
            return (ViewSkinCurrent) current;
        }
        return null;
    }

    public void dispatch(View view, int skinIndex) {
        if (view == null) {
            return;
        }
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Trace.beginSection("UISkin::dispatch");
        }
        SkinItem skinItem = mSkins.get(skinIndex);
        Resources.Theme theme;
        if (skinItem == null) {
            if (skinIndex != DEFAULT_SKIN) {
                throw new IllegalArgumentException("The skin " + skinIndex + " does not exist");
            }
            theme = view.getContext().getTheme();
        } else {
            theme = skinItem.getTheme();
        }
        runDispatch(view, skinIndex, theme);
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Trace.endSection();
        }
    }


    private void runDispatch(@NonNull View view, int skinIndex, Resources.Theme theme) {
        ViewSkinCurrent currentTheme = getViewSkinCurrent(view);
        if(currentTheme != null && currentTheme.index == skinIndex && Objects.equals(currentTheme.managerName, mName)){
            return;
        }
        view.setTag(R.id.ui_skin_current, new ViewSkinCurrent(mName, skinIndex));

        if (view instanceof IUISkinDispatchInterceptor) {
            if (((IUISkinDispatchInterceptor) view).intercept(skinIndex, theme)) {
                return;
            }
        }
        applyTheme(view, skinIndex, theme);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (useHierarchyChangeListener(viewGroup)) {
                viewGroup.setOnHierarchyChangeListener(mOnHierarchyChangeListener);
            } else {
                viewGroup.addOnLayoutChangeListener(mOnLayoutChangeListener);
            }
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                runDispatch(viewGroup.getChildAt(i), skinIndex, theme);
            }
        } else if ((view instanceof TextView) || (view instanceof UIQQFaceView)) {
            CharSequence text;
            if (view instanceof TextView) {
                text = ((TextView) view).getText();
            } else {
                text = ((UIQQFaceView) view).getText();
            }
            if (text instanceof Spanned) {
                IUISkinHandlerSpan[] spans = ((Spanned) text).getSpans(0, text.length(), IUISkinHandlerSpan.class);
                if (spans != null) {
                    for (int i = 0; i < spans.length; i++) {
                        spans[i].handle(view, this, skinIndex, theme);
                    }
                }
                view.invalidate();
            }
        }
    }

    private boolean useHierarchyChangeListener(ViewGroup viewGroup) {
        return viewGroup instanceof RecyclerView ||
                viewGroup instanceof ViewPager ||
                viewGroup instanceof AdapterView ||
                viewGroup.getClass().isAnnotationPresent(UISkinListenWithHierarchyChange.class);
    }

    private void applyTheme(@NonNull View view, int skinIndex, Resources.Theme theme) {
        SimpleArrayMap<String, Integer> attrs = getSkinAttrs(view);
        try{
            if (view instanceof IUISkinHandlerView) {
                ((IUISkinHandlerView) view).handle(this, skinIndex, theme, attrs);
            } else {
                defaultHandleSkinAttrs(view, theme, attrs);
            }
            if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
                int itemDecorationCount = recyclerView.getItemDecorationCount();
                for (int i = 0; i < itemDecorationCount; i++) {
                    RecyclerView.ItemDecoration itemDecoration = recyclerView.getItemDecorationAt(i);
                    if (itemDecoration instanceof IUISkinHandlerDecoration) {
                        ((IUISkinHandlerDecoration) itemDecoration).handle(recyclerView, this, skinIndex, theme);
                    }
                }
            }
        }catch (Throwable throwable){
            UILog.printErrStackTrace(TAG, throwable,
                    "catch error when apply theme: " + view.getClass().getSimpleName() +
                            "; " + skinIndex + "; attrs = " + (attrs == null ? "null" : attrs.toString()));
        }
    }

    void refreshRecyclerDecoration(@NonNull RecyclerView recyclerView,
                                 @NonNull IUISkinHandlerDecoration decoration,
                                 int skinIndex){
        SkinItem skinItem = mSkins.get(skinIndex);
        if (skinItem != null) {
            decoration.handle(recyclerView, this, skinIndex, skinItem.getTheme());
        }
    }

    void refreshTheme(@NonNull View view, int skinIndex) {
        SkinItem skinItem = mSkins.get(skinIndex);
        if (skinItem != null) {
            applyTheme(view, skinIndex, skinItem.getTheme());
        }
    }

    public void defaultHandleSkinAttrs(@NonNull View view, Resources.Theme theme, @Nullable SimpleArrayMap<String, Integer> attrs) {
        if (attrs != null) {
            for (int i = 0; i < attrs.size(); i++) {
                String key = attrs.keyAt(i);
                Integer attr = attrs.valueAt(i);
                if (attr == null) {
                    continue;
                }
                defaultHandleSkinAttr(view, theme, key, attr);
            }
        }
    }

    public void defaultHandleSkinAttr(View view, Resources.Theme theme, String name, int attr) {
        if (attr == 0) {
            return;
        }
        IUISkinRuleHandler handler = sRuleHandlers.get(name);
        if (handler == null) {
            UILog.w(TAG, "Do not find handler for skin attr name: " + name);
            return;
        }
        handler.handle(this, view, theme, name, attr);
    }

    @Nullable
    private SimpleArrayMap<String, Integer> getSkinAttrs(View view) {
        String skinValue = (String) view.getTag(R.id.ui_skin_value);
        String[] items;
        if (skinValue == null || skinValue.isEmpty()) {
            items = EMPTY_ITEMS;
        } else {
            items = skinValue.split("[|]");
        }

        SimpleArrayMap<String, Integer> attrs = null;
        if (view instanceof IUISkinDefaultAttrProvider) {
            SimpleArrayMap<String, Integer> defaultAttrs = ((IUISkinDefaultAttrProvider) view).getDefaultSkinAttrs();
            if(defaultAttrs != null && !defaultAttrs.isEmpty()){
                attrs = new SimpleArrayMap<>(defaultAttrs);
            }
        }
        IUISkinDefaultAttrProvider provider = (IUISkinDefaultAttrProvider) view.getTag(
                R.id.ui_skin_default_attr_provider);
        if (provider != null) {
            SimpleArrayMap<String, Integer> providedAttrs = provider.getDefaultSkinAttrs();
            if(providedAttrs != null && !providedAttrs.isEmpty()){
                if (attrs != null) {
                    attrs.putAll(providedAttrs);
                } else {
                    attrs = new SimpleArrayMap<>(providedAttrs);
                }
            }
        }

        if (attrs == null) {
            if(items.length <= 0){
                return null;
            }
            attrs = new SimpleArrayMap<>(items.length);
        }

        for (String item : items) {
            String[] kv = item.split(":");
            if (kv.length != 2) {
                continue;
            }
            String key = kv[0].trim();
            if (UILangHelper.isNullOrEmpty(key)) {
                continue;
            }
            int attr = getAttrFromName(kv[1].trim());
            if (attr == 0) {
                UILog.w(TAG, "Failed to get attr id from name: " + kv[1]);
                continue;
            }
            attrs.put(key, attr);
        }
        return attrs;
    }

    public int getAttrFromName(String attrName) {
        return mResources.getIdentifier(attrName, "attr", mPackageName);
    }

    class SkinItem {
        private int styleRes;

        SkinItem(int styleRes) {
            this.styleRes = styleRes;
        }

        public int getStyleRes() {
            return styleRes;
        }

        @NonNull
        Resources.Theme getTheme() {
            Resources.Theme theme = sStyleIdThemeMap.get(styleRes);
            if (theme == null) {
                theme = mResources.newTheme();
                theme.applyStyle(styleRes, true);
                sStyleIdThemeMap.put(styleRes, theme);
            }
            return theme;
        }
    }

    // =====================================================================================

    private int mCurrentSkin = DEFAULT_SKIN;
    private final List<WeakReference<?>> mSkinObserverList = new ArrayList<>();
    private final List<WeakReference<OnSkinChangeListener>> mSkinChangeListeners = new ArrayList<>();

    public void register(@NonNull Activity activity) {
        if (!containSkinObserver(activity)) {
            mSkinObserverList.add(new WeakReference<>(activity));
        }
        dispatch(activity.findViewById(Window.ID_ANDROID_CONTENT), mCurrentSkin);
    }

    public void unRegister(@NonNull Activity activity) {
        removeSkinObserver(activity);
    }

    public void register(@NonNull Fragment fragment) {
        if (!containSkinObserver(fragment)) {
            mSkinObserverList.add(new WeakReference<>(fragment));
        }
        dispatch(fragment.getView(), mCurrentSkin);
    }

    public void unRegister(@NonNull Fragment fragment) {
        removeSkinObserver(fragment);
    }

    public void register(@NonNull View view) {
        if (!containSkinObserver(view)) {
            mSkinObserverList.add(new WeakReference<>(view));
        }
        dispatch(view, mCurrentSkin);
    }

    public void unRegister(@NonNull View view) {
        removeSkinObserver(view);
    }

    public void register(@NonNull Dialog dialog) {
        if (!containSkinObserver(dialog)) {
            mSkinObserverList.add(new WeakReference<>(dialog));
        }
        Window window = dialog.getWindow();
        if (window != null) {
            dispatch(window.getDecorView(), mCurrentSkin);
        }
    }

    public void unRegister(@NonNull Dialog dialog) {
        removeSkinObserver(dialog);
    }

    public void register(@NonNull PopupWindow popupWindow) {
        if (!containSkinObserver(popupWindow)) {
            mSkinObserverList.add(new WeakReference<>(popupWindow));
        }
        dispatch(popupWindow.getContentView(), mCurrentSkin);
    }

    public void unRegister(@NonNull PopupWindow popupWindow) {
        removeSkinObserver(popupWindow);
    }

    public void register(@NonNull Window window) {
        if (!containSkinObserver(window)) {
            mSkinObserverList.add(new WeakReference<>(window));
        }
        dispatch(window.getDecorView(), mCurrentSkin);
    }

    public void unRegister(@NonNull Window window) {
        removeSkinObserver(window);
    }

    private void removeSkinObserver(Object object) {
        for (int i = mSkinObserverList.size() - 1; i >= 0; i--) {
            Object item = mSkinObserverList.get(i).get();
            if (item == object) {
                mSkinObserverList.remove(i);
                return;
            } else if (item == null) {
                mSkinObserverList.remove(i);
            }
        }
    }

    private boolean containSkinObserver(Object object) {
        //reverse order for remove
        for (int i = mSkinObserverList.size() - 1; i >= 0; i--) {
            Object item = mSkinObserverList.get(i).get();
            if (item == object) {
                return true;
            } else if (item == null) {
                mSkinObserverList.remove(i);
            }
        }
        return false;
    }

    public void changeSkin(int index) {
        if (mCurrentSkin == index) {
            return;
        }
        int oldIndex = mCurrentSkin;
        mCurrentSkin = index;
        for (int i = mSkinObserverList.size() - 1; i >= 0; i--) {
            Object item = mSkinObserverList.get(i).get();
            if (item == null) {
                mSkinObserverList.remove(i);
            } else {
                if (item instanceof Activity) {
                    Activity activity = (Activity) item;
                    activity.getWindow().setBackgroundDrawable(UIResHelper.getAttrDrawable(
                            activity, mSkins.get(index).getTheme(), R.attr.ui_skin_support_activity_background));
                    dispatch(activity.findViewById(Window.ID_ANDROID_CONTENT), index);
                } else if (item instanceof Fragment) {
                    dispatch(((Fragment) item).getView(), index);
                } else if (item instanceof Dialog) {
                    Window window = ((Dialog) item).getWindow();
                    if (window != null) {
                        dispatch(window.getDecorView(), index);
                    }
                } else if (item instanceof PopupWindow) {
                    dispatch(((PopupWindow) item).getContentView(), index);
                } else if (item instanceof Window) {
                    dispatch(((Window) item).getDecorView(), index);
                } else if (item instanceof View) {
                    dispatch((View) item, index);
                }
            }
        }

        for (int i = mSkinChangeListeners.size() - 1; i >= 0; i--) {
            OnSkinChangeListener item = mSkinChangeListeners.get(i).get();
            if (item == null) {
                mSkinChangeListeners.remove(i);
            } else {
                item.onSkinChange(this, oldIndex, mCurrentSkin);
            }
        }
    }

    public void addSkinChangeListener(@NonNull OnSkinChangeListener listener) {
        Iterator<WeakReference<OnSkinChangeListener>> iterator = mSkinChangeListeners.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next().get();
            if (item != null) {
                return;
            } else {
                iterator.remove();
            }
        }
        mSkinChangeListeners.add(new WeakReference<>(listener));
    }

    public void removeSkinChangeListener(@NonNull OnSkinChangeListener listener) {
        Iterator<WeakReference<OnSkinChangeListener>> iterator = mSkinChangeListeners.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next().get();
            if (item != null) {
                if (item == listener) {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
    }

    public int getCurrentSkin() {
        return mCurrentSkin;
    }

    public interface OnSkinChangeListener {
        void onSkinChange(UISkinManager skinManager, int oldSkin, int newSkin);
    }

    class ViewSkinCurrent{
        String managerName;
        int index;
        ViewSkinCurrent(String managerName, int index){
            this.managerName = managerName;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ViewSkinCurrent that = (ViewSkinCurrent) o;
            return index == that.index &&
                    Objects.equals(managerName, that.managerName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(managerName, index);
        }
    }
}
