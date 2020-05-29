package com.batman.baselibrary.im;

import android.util.Log;

import com.batman.baselibrary.im.data.MsgData;
import com.batman.baselibrary.im.handler.IMessageHandler;
import com.batman.baselibrary.im.handler.MessageHandlerFactory;
import com.batman.baselibrary.im.utils.CThreadPoolExecutor;
import com.network.im.data.IMBody;


/**
 * 消息处理器
 */
public class MessageProcessor implements IMessageProcessor {

    private static final String TAG = MessageProcessor.class.getSimpleName();

    private MessageProcessor() {

    }

    private static class MessageProcessorInstance {
        private static final IMessageProcessor INSTANCE = new MessageProcessor();
    }

    public static IMessageProcessor getInstance() {
        return MessageProcessorInstance.INSTANCE;
    }

    /**
     * 接收消息
     *
     * @param message
     */
    @Override
    public void receiveMsg(final MsgData message) {


        CThreadPoolExecutor.runInBackground(() -> {
            try {

                IMessageHandler messageHandler = MessageHandlerFactory.getHandlerByMsgType(message.type);
                if (messageHandler != null) {
                    messageHandler.execute(message);
                } else {
                    Log.e(TAG, "未找到消息处理handler");
                }
            } catch (Exception e) {
                Log.e(TAG, "消息处理出错，reason=" + e.getMessage());
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(final MsgData message) {
        CThreadPoolExecutor.runInBackground(new Runnable() {

            @Override
            public void run() {
                boolean isActive = IMSClientBootstrap.getInstance().isActive();
                if (isActive) {
                    IMSClientBootstrap.getInstance().sendMessage(MessageBuilder.buildAppMessage(message));
                } else {
                    Log.e(TAG, "发送消息失败");
                }
            }
        });
    }

}
