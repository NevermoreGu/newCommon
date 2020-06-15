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

package com.batman.ui.widget.popup;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.batman.ui.widget.UIWrapContentListView;

public class UIPopups {

    public static UIPopup popup(Context context) {
        return new UIPopup(context,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static UIPopup popup(Context context, int width) {
        return new UIPopup(context,
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static UIPopup popup(Context context, int width, int height) {
        return new UIPopup(context, width, height);
    }

    /**
     * show a list with popup
     *
     * @param context             activity context
     * @param width               the with for the popup content
     * @param maxHeight           the max height of popup, it is scrollable if the content is higher then maxHeight
     * @param adapter             the adapter for the list view
     * @param onItemClickListener the onItemClickListener for list item view
     * @return UIPopup
     */
    public static UIPopup listPopup(Context context, int width, int maxHeight,
                                      BaseAdapter adapter,
                                      AdapterView.OnItemClickListener onItemClickListener) {
        ListView listView = new UIWrapContentListView(context, maxHeight);
        listView.setAdapter(adapter);
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setDivider(null);
        return popup(context, width).view(listView);
    }

    public static UIFullScreenPopup fullScreenPopup(Context context) {
        return new UIFullScreenPopup(context);
    }

    public static UIQuickAction quickAction(Context context, int actionWidth, int actionHeight) {
        return new UIQuickAction(context, ViewGroup.LayoutParams.WRAP_CONTENT, actionHeight)
                .actionWidth(actionWidth)
                .actionHeight(actionHeight);
    }
}
