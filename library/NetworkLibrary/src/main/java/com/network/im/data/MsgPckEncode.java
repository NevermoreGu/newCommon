package com.network.im.data;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MsgPckEncode extends MessageToByteEncoder<IMBody> {

    /**
     * 编码信息
     *
     * @param ctx 通信管道
     * @param msg 通信内容
     * @param out 内容字节流
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, IMBody msg, ByteBuf out) throws Exception {
        byte[] data = msg.getBody().getBytes(CharsetUtil.UTF_8);
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
