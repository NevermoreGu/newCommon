package com.network.im;


import com.network.im.data.ImData;
import com.network.im.netty.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthRespHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public AuthRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }


        if (imsClient.isHandShakeMsg(msg)) {
            System.out.println("收到服务端握手响应消息，message");

            if (imsClient.isAuthAgree(msg)) {
                // 握手成功，马上先发送一条心跳消息，至于心跳机制管理，交由HeartbeatHandler
//                ImData heartbeatMsg = imsClient.getHeartbeatMsg();
//                if (heartbeatMsg == null) {
//                    return;
//                }

                // 握手成功，检查消息发送超时管理器里是否有发送超时的消息，如果有，则全部重发
//                imsClient.getMsgTimeoutTimerManager().onResetConnected();

//                System.out.println("发送心跳消息：" + heartbeatMsg);
//                imsClient.sendMsg(heartbeatMsg);

                // 添加心跳消息管理handler
                imsClient.addHeartbeatHandler();
                //添加心跳 写
                AlarmManagerUtils alarmManagerUtils = AlarmManagerUtils.getInstance(imsClient.mContext);
                alarmManagerUtils.createGetUpAlarmManager();
                alarmManagerUtils.getUpAlarmManagerStartWork();

            } else {
                imsClient.resetConnect(false);// 握手失败，触发重连
            }

        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
