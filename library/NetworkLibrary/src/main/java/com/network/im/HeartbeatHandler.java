package com.network.im;


import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.network.im.data.ImData;
import com.network.im.netty.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳任务管理器
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public HeartbeatHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state) {
                case READER_IDLE: {
                    // 规定时间内没收到服务端心跳包响应，进行重连操作
                    imsClient.resetConnect(false);
                    break;
                }

                case WRITER_IDLE: {
                    // 规定时间内没向服务端发送心跳包，即发送一个心跳包
//                    if (heartbeatTask == null) {
//                        heartbeatTask = new HeartbeatTask(ctx);
//                    }
//                    imsClient.getLoopGroup().execWorkTask(heartbeatTask);
                    break;
                }
            }
        }
    }

    private HeartbeatTask heartbeatTask;

    private class HeartbeatTask implements Runnable {

        private ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (ctx.channel().isActive()) {
                ImData heartbeatMsg = imsClient.getHeartbeatMsg();
                if (heartbeatMsg == null) {
                    return;
                }
                System.out.println("发送心跳消息，message=" + heartbeatMsg);
                imsClient.sendMsg(heartbeatMsg, false);
            }
        }
    }

}
