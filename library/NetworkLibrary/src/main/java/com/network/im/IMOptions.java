package com.network.im;

public class IMOptions {

    // 重连间隔时长
    private int reconnectInterval;
    // 连接超时时长
    private int connectTimeout;
    // 心跳间隔时间
    private int heartbeatInterval;
    // 应用在后台时心跳间隔时间
    private int foregroundHeartbeatInterval;
    // 应用在前台时心跳间隔时间
    private int backgroundHeartbeatInterval;

    // 消息发送超时重发次数
    private int resendCount;
    // 消息发送失败重发间隔时长
    private int resendInterval;

    public static IMOptions getDefault() {
        return new Builder()
                .reconnectInterval(IMSConfig.DEFAULT_RECONNECT_BASE_DELAY_TIME)
                .connectTimeout(IMSConfig.DEFAULT_CONNECT_TIMEOUT)
                .heartbeatInterval(IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND)
                .foregroundHeartbeatInterval(IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND)
                .backgroundHeartbeatInterval(IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND)
                .resendCount(IMSConfig.DEFAULT_RESEND_COUNT)
                .resendInterval(IMSConfig.DEFAULT_RESEND_INTERVAL)
                .build();
    }

    private IMOptions(Builder builder) {
        reconnectInterval = builder.reconnectInterval;
        connectTimeout = builder.connectTimeout;
        heartbeatInterval = builder.heartbeatInterval;
        foregroundHeartbeatInterval = builder.foregroundHeartbeatInterval;
        backgroundHeartbeatInterval = builder.backgroundHeartbeatInterval;
        resendCount = builder.resendCount;
        resendInterval = builder.resendInterval;
    }

    public static final class Builder {
        private int reconnectInterval;
        private int connectTimeout;
        private int heartbeatInterval;
        private int foregroundHeartbeatInterval;
        private int backgroundHeartbeatInterval;
        private int appStatus;
        private int resendCount;
        private int resendInterval;

        public Builder() {
        }

        public Builder reconnectInterval(int val) {
            reconnectInterval = val;
            return this;
        }

        public Builder connectTimeout(int val) {
            connectTimeout = val;
            return this;
        }

        public Builder heartbeatInterval(int val) {
            heartbeatInterval = val;
            return this;
        }

        public Builder foregroundHeartbeatInterval(int val) {
            foregroundHeartbeatInterval = val;
            return this;
        }

        public Builder backgroundHeartbeatInterval(int val) {
            backgroundHeartbeatInterval = val;
            return this;
        }

        public Builder resendCount(int val) {
            resendCount = val;
            return this;
        }

        public Builder resendInterval(int val) {
            resendInterval = val;
            return this;
        }

        public IMOptions build() {
            return new IMOptions(this);
        }
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public int getForegroundHeartbeatInterval() {
        return foregroundHeartbeatInterval;
    }

    public int getBackgroundHeartbeatInterval() {
        return backgroundHeartbeatInterval;
    }


    public int getResendCount() {
        return resendCount;
    }

    public int getResendInterval() {
        return resendInterval;
    }
}
