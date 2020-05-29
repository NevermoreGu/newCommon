package com.network.im.data;

import java.io.Serializable;

public interface ImData extends Serializable {
    /**
     * 数据转化 发送的数据
     *
     * @return 将要发送的数据的字节数组
     */
    Object parse();

}
