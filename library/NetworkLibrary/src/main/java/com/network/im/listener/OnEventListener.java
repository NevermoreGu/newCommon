package com.network.im.listener;


import com.network.im.data.ImData;

/**
 * 与应用层交互的listener
 */
public interface OnEventListener {

    /**
     * 分发消息到应用层
     *
     * @param msg
     */
    void dispatchMsg(Object msg);

    /**
     * 获取由应用层构造的握手消息
     *
     * @return
     */
    ImData getHandshakeMsg();

    /**
     * 获取由应用层构造的心跳消息
     *
     * @return
     */
    ImData getHeartbeatMsg();

    boolean isHandShakeMsg(Object msg);

    boolean isAuthAgree(Object msg);

    boolean isHeartbeatMsg(Object msg);
}
