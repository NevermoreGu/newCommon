package com.batman.baselibrary.im.handler;

import android.util.SparseArray;

import com.batman.baselibrary.im.MessageType;


/**
 * 消息处理handler工厂
 */
public class MessageHandlerFactory {

    private MessageHandlerFactory() {

    }

    private static final SparseArray<IMessageHandler> HANDLERS = new SparseArray<>();

    static {
        /** 单聊消息处理handler */
        HANDLERS.put(MessageType.SINGLE_CHAT.getMsgType(), new SingleChatMessageHandler());
    }

    /**
     * 根据消息类型获取对应的处理handler
     *
     * @param msgType
     * @return
     */
    public static IMessageHandler getHandlerByMsgType(int msgType) {
        return HANDLERS.get(msgType);
    }
}
