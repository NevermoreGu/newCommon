package com.media.image.load;

import android.content.Context;

public interface IImageLoader {

    void loadImage(Context context, ImageLoader imageLoader);

    void clearMemoryCache(Context ctx);
}
