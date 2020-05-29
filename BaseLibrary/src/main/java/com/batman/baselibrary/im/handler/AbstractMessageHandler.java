package com.batman.baselibrary.im.handler;


import com.batman.baselibrary.im.data.MsgData;

/**
 * 抽象的MessageHandler
 */
public abstract class AbstractMessageHandler implements IMessageHandler {

    @Override
    public void execute(MsgData message) {
        action(message);
    }

    protected abstract void action(MsgData message);
}
