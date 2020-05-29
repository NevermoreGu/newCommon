package com.batman.baselibrary.im.handler;


import com.batman.baselibrary.im.data.MsgData;

/**
 * 类描述
 */
public interface IMessageHandler {

    void execute(MsgData message);
}
