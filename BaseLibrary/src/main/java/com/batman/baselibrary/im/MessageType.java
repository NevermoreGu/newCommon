package com.batman.baselibrary.im;

/**
 * 消息类型
 */
public enum MessageType {

    /*
     * 握手消息
     */
    HANDSHAKE(1),

    /*
     * 心跳消息
     */
    HEARTBEAT(55),

    /**
     * 单聊消息
     */
    SINGLE_CHAT(10),

    /**
     * 群聊消息
     */
    GROUP_CHAT(3001);

    private int msgType;

    MessageType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public enum MessageContentType {

        /**
         * 文本消息
         */
        TEXT(101),

        /**
         * 图片消息
         */
        IMAGE(102),

        /**
         * 语音消息
         */
        VOICE(103);

        private int msgContentType;

        MessageContentType(int msgContentType) {
            this.msgContentType = msgContentType;
        }

        public int getMsgContentType() {
            return this.msgContentType;
        }
    }
}
