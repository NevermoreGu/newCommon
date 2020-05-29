package com.network.im;


import com.network.im.netty.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {


    private NettyTcpClient imsClient;

    public HeartbeatRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }

        if (imsClient.isHeartbeatMsg(msg)) {
            System.out.println("收到服务端心跳响应消息，message=");
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
