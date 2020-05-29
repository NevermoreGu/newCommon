package com.network.im;


import android.content.Context;

import com.network.im.interf.IMSClientInterface;
import com.network.im.netty.NettyTcpClient;


public class IMSClientFactory {

    public static IMSClientInterface getIMSClient(Context context) {
        return NettyTcpClient.getInstance(context);
    }
}
