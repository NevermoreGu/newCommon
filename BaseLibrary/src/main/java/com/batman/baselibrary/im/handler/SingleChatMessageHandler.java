package com.batman.baselibrary.im.handler;

import android.util.Log;

import com.batman.baselibrary.im.MessageProcessor;
import com.batman.baselibrary.im.data.MsgData;
import com.network.im.data.IMBody;


/**
 * 类描述
 */
public class SingleChatMessageHandler extends AbstractMessageHandler {

    private static final String TAG = SingleChatMessageHandler.class.getSimpleName();

    @Override
    protected void action(MsgData message) {
        Log.d(TAG, "收到单聊消息，message=" + message);

        MsgData msgData = new MsgData();
        msgData.type = 10;
        msgData.body = "收到";
        MessageProcessor.getInstance().sendMsg(msgData);
    }
}
