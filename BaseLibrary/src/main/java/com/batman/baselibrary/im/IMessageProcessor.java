package com.batman.baselibrary.im;


import com.batman.baselibrary.im.data.MsgData;

/**
 * 消息处理器接口
 */
public interface IMessageProcessor {

    void receiveMsg(MsgData message);

    void sendMsg(MsgData message);

}
