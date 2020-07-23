package com.media.image.load;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

public class GlideImageLoader implements IImageLoader {

    @Override
    public void loadImage(Context ctx, ImageLoader img) {

        /**
         * 检查 wifi下下载图片是否开启，如果开启检查是否wifi状态下
         */
        boolean flag;
        flag = isFlag(ctx);
//        if (flag) {
            loadNormal(ctx, img, null);
//        } else {
//            loadCache(ctx, img);
//        }
    }

    private boolean isFlag(Context ctx) {
//        boolean flag;
//        if (MyApplication.CheckWifi) {
//            flag = TDevice.isWifiOpen();
//        } else {
//            flag = true;
//        }
        return true;
    }

    @Override
    public void clearMemoryCache(Context ctx) {
        Glide.get(ctx).clearMemory();
    }

    /**
     * load image with Glide
     */
    private void loadNormal(Context ctx, ImageLoader img, RequestListener listener) {
//        Glide.with(ctx).load(img.url()).placeholder(img.placeHolder()).into(img.imgView());
        Glide.with(ctx).asBitmap().load(img.getUrl()).placeholder(img.getPlaceHolder()).listener(listener).into(img.getImgView());
    }


    /**
     * load cache image with Glide
     */
//    private void loadCache(Context ctx, ImageLoader img) {
//        Glide.with(ctx).using(new StreamModelLoader<String>() {
//            @Override
//            public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
//                return new DataFetcher<InputStream>() {
//                    @Override
//                    public InputStream loadData(Priority priority) throws Exception {
//                        throw new IOException();
//                    }
//
//                    @Override
//                    public void cleanup() {
//
//                    }
//
//                    @Override
//                    public String getId() {
//                        return model;
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                };
//            }
//        }).load(img.getUrl()).placeholder(img.getPlaceHolder()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img.getImgView());
//    }
}
