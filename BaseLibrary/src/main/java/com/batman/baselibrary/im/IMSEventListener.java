package com.batman.baselibrary.im;

import com.batman.baselibrary.im.data.MsgData;
import com.google.gson.Gson;
import com.network.im.data.ImData;
import com.network.im.data.IMBody;
import com.network.im.listener.OnEventListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 与ims交互的listener
 */
public class IMSEventListener implements OnEventListener {

    private String userId;
    private String token;

    public IMSEventListener(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    /**
     * 接收ims转发过来的消息
     *
     * @param msg
     */
    @Override
    public void dispatchMsg(Object msg) {
        MessageProcessor.getInstance().receiveMsg(MessageBuilder.getMessageByProtobuf(msg));
    }

    /**
     * 构建握手消息
     *
     * @return
     */
    @Override
    public ImData getHandshakeMsg() {

        ImData imData = (ImData) () -> {

            MsgData msgData = new MsgData();
            msgData.body = "123";
            msgData.type = MessageType.HANDSHAKE.getMsgType();

            Gson gson = new Gson();

//            String content = "";
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("type", MessageType.HANDSHAKE);
//                jsonObject.put("body", "123");
//                content = jsonObject.toString();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            return new IMBody(gson.toJson(msgData));
        };
        return imData;
    }

    /**
     * 构建心跳消息
     *
     * @return
     */
    @Override
    public ImData getHeartbeatMsg() {

        ImData imData = (ImData) () -> {

            MsgData msgData = new MsgData();
            msgData.body = "heart";
            msgData.type = MessageType.HEARTBEAT.getMsgType();

            Gson gson = new Gson();

//            String content = "";
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("type", MessageType.HANDSHAKE);
//                jsonObject.put("body", "123");
//                content = jsonObject.toString();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            return new IMBody(gson.toJson(msgData));
        };
        return imData;
    }

    @Override
    public boolean isHandShakeMsg(Object msg) {
        MsgData msgData = MessageBuilder.getMessageByProtobuf(msg);
        if (msgData.type == MessageType.HANDSHAKE.getMsgType()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isAuthAgree(Object msg) {
        MsgData msgData = MessageBuilder.getMessageByProtobuf(msg);
        return true;
    }

    @Override
    public boolean isHeartbeatMsg(Object msg) {
        MsgData msgData = MessageBuilder.getMessageByProtobuf(msg);
        if (msgData.type == MessageType.HEARTBEAT.getMsgType()) {
            return true;
        }
        return false;
    }

}
