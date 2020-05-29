package com.network.im.data;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class MsgPckDecode extends LengthFieldBasedFrameDecoder {

    /**
     * 请求头的长度
     */
    private static final int HEADER_SIZE = 4;

    public MsgPckDecode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    /**
     * 解码通信内容
     *
     * @param ctx 连接通道
     * @param in 接受文件流
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }

        if (in.readableBytes() <= HEADER_SIZE) {
            return null;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return null;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        String body = new String(data, CharsetUtil.UTF_8);
        IMBody msg = new IMBody(body);
        return msg;
    }
}
