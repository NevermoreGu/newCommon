package com.batman.logincomponent.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.batman.baselibrary.base.BaseActivity;
import com.batman.logincomponent.R2;
import com.batman.ui.widget.UINavigationView;

import butterknife.BindView;

/**
 * Created by guqian on 2017/8/2.
 */

public class ServiceWebActivity extends BaseActivity {

    @BindView(R2.id.uinv)
    UINavigationView mUinv;

    public String mUrl;

    public String mTitle;

    public static void start(Context context, String url, String title) {

        Intent intent = new Intent();
        intent.setClass(context, ServiceWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData(Bundle savedInstanceState) {

    }

//    @Override
//    public void loadData(Bundle savedInstanceState) {
//        super.loadData(savedInstanceState);
//        mUrl = getIntent().getStringExtra("url");
//        mTitle = getIntent().getStringExtra("title");
//        mUinv.setNavigationTitle(mTitle);
//        mWebView.loadUrl(mUrl);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mWebView != null) {
//            mWebView.destroy();
//        }
//    }
}
