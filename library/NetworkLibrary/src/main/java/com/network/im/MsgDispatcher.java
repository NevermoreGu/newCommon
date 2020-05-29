package com.network.im;


import com.network.im.listener.OnEventListener;

public class MsgDispatcher {

    private OnEventListener mOnEventListener;

    public MsgDispatcher() {

    }

    public void setOnEventListener(OnEventListener listener) {
        this.mOnEventListener = listener;
    }

    /**
     * 接收消息，并通过OnEventListener转发消息到应用层
     *
     * @param msg
     */
    public void receivedMsg(Object msg) {
        if (mOnEventListener == null) {
            return;
        }

        mOnEventListener.dispatchMsg(msg);
    }
}
