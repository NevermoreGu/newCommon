package com.batman.baselibrary.im;

import com.batman.baselibrary.im.data.MsgData;
import com.google.gson.Gson;
import com.network.im.data.IMBody;
import com.network.im.data.ImData;

/**
 * 消息转换
 */
public class MessageBuilder {

    /**
     * 根据业务消息对象获取protoBuf消息对应的builder
     *
     * @param message
     * @return
     */
    public static ImData buildAppMessage(MsgData message) {

        ImData imData = (ImData) () -> {

            Gson gson = new Gson();
            return new IMBody(gson.toJson(message));
        };
        return imData;

    }

    /**
     * 通过protobuf消息对象获取业务消息对象
     *
     * @return
     */
    public static MsgData getMessageByProtobuf(
            Object object) {
        Gson gson = new Gson();
        IMBody body = (IMBody) object;
        MsgData message = gson.fromJson(body.getBody(), MsgData.class);
        return message;
    }
}
