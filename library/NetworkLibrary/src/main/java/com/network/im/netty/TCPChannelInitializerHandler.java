package com.network.im.netty;


import com.network.im.AuthRespHandler;
import com.network.im.HeartbeatRespHandler;
import com.network.im.data.MsgPckDecode;
import com.network.im.data.MsgPckEncode;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Channel初始化配置
 */
public class TCPChannelInitializerHandler extends ChannelInitializer<Channel> {

    private NettyTcpClient imsClient;

    public TCPChannelInitializerHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // netty提供的自定义长度解码器，解决TCP拆包/粘包问题
//        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
//        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
//                0, 2, 0, 2));

        pipeline.addLast(new MsgPckDecode(1 << 20, 10, 4));
        pipeline.addLast(new MsgPckEncode());
        // 增加protobuf编解码支持
//        pipeline.addLast(new ProtobufEncoder());
//        pipeline.addLast(new ProtobufDecoder(ImData.getDefaultInstance()));

        // 握手认证消息响应处理handler
        pipeline.addLast(AuthRespHandler.class.getSimpleName(), new AuthRespHandler(imsClient));
        // 心跳消息响应处理handler
        pipeline.addLast(HeartbeatRespHandler.class.getSimpleName(), new HeartbeatRespHandler(imsClient));
        // 接收消息处理handler
        pipeline.addLast(TCPReadHandler.class.getSimpleName(), new TCPReadHandler(imsClient));
    }
}
