package com.network.im.interf;


import com.network.im.MsgDispatcher;
import com.network.im.data.ImData;
import com.network.im.listener.IMSConnectStatusCallback;
import com.network.im.listener.OnEventListener;

import java.util.Vector;

/**
 * ims抽象接口，需要切换到其它方式实现im功能，实现此接口即可
 */
public interface IMSClientInterface {

    /**
     * 初始化
     *
     * @param serverUrlList 服务器地址列表
     * @param listener      与应用层交互的listener
     * @param callback      ims连接状态回调
     */
    void init(Vector<String> serverUrlList, OnEventListener listener, IMSConnectStatusCallback callback);

    /**
     * 重置连接，也就是重连
     * 首次连接也可认为是重连
     */
    void resetConnect();

    /**
     * 重置连接，也就是重连
     * 首次连接也可认为是重连
     * 重载
     *
     * @param isFirst 是否首次连接
     */
    void resetConnect(boolean isFirst);

    /**
     * 关闭连接，同时释放资源
     */
    void close();

    /**
     * 标识ims是否已关闭
     *
     * @return
     */
    boolean isClosed();

    /**
     * 发送消息
     *
     * @param msg
     */
    void sendMsg(ImData msg);

    /**
     * 发送消息
     * 重载
     *
     * @param msg
     * @param isJoinTimeoutManager 是否加入发送超时管理器
     */
    void sendMsg(ImData msg, boolean isJoinTimeoutManager);

    /**
     * 获取消息转发器
     *
     * @return
     */
    MsgDispatcher getMsgDispatcher();

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
