package com.network.im.data;

import io.netty.util.CharsetUtil;

public class IMBody{

    /**
     * 包体长度
     */
    private int length;

    /**
     * 包体内容
     */
    private String body;

    public IMBody() {

    }

    public IMBody(byte[] data) {
        this.length = data.length;
        this.body = new String(data, CharsetUtil.UTF_8);
    }

    public IMBody(String body) {
        this.length = body.getBytes(CharsetUtil.UTF_8).length;
        this.body = body;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
