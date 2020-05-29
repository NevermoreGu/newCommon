//package com.network.im;
//
//import com.network.im.data.ImData;
//import com.network.im.interf.IMSClientInterface;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MsgTimeoutTimer extends Timer {
//
//    private IMSClientInterface imsClient;// ims客户端
//    private ImData msg;// 发送的消息
//    private int currentResendCount;// 当前重发次数
//    private MsgTimeoutTask task;// 消息发送超时任务
//
//    public MsgTimeoutTimer(IMSClientInterface imsClient, ImData msg) {
//        this.imsClient = imsClient;
//        this.msg = msg;
//        task = new MsgTimeoutTask();
//        this.schedule(task, imsClient.getResendInterval(), imsClient.getResendInterval());
//    }
//
//    /**
//     * 消息发送超时任务
//     */
//    private class MsgTimeoutTask extends TimerTask {
//
//        @Override
//        public void run() {
//            if (imsClient.isClosed()) {
//                if (imsClient.getMsgTimeoutTimerManager() != null) {
//                    imsClient.getMsgTimeoutTimerManager().remove(msg.getHead().getMsgId());
//                }
//
//                return;
//            }
//
//            currentResendCount++;
//            if (currentResendCount > imsClient.getResendCount()) {
//                // 重发次数大于可重发次数，直接标识为发送失败，并通过消息转发器通知应用层
//                try {
//                    ImData.Builder builder = new ImData.Builder();
//                    ImData.Head.Builder headBuilder = new ImData.Head.Builder();
//                    headBuilder.setMsgId(msg.getHead().getMsgId());
//                    headBuilder.setMsgType(imsClient.getServerSentReportMsgType());
//                    headBuilder.setTimestamp(System.currentTimeMillis() + "");
//                    headBuilder.setStatusReport(IMSConfig.DEFAULT_REPORT_SERVER_SEND_MSG_FAILURE);
//                    builder.setHead(headBuilder.build());
//
//                    // 通知应用层消息发送失败
//                    imsClient.getMsgDispatcher().receivedMsg(builder.build());
//                } finally {
//                    // 从消息发送超时管理器移除该消息
//                    imsClient.getMsgTimeoutTimerManager().remove(msg.getHead().getMsgId());
//                    // 执行到这里，认为连接已断开或不稳定，触发重连
//                    imsClient.resetConnect();
//                    currentResendCount = 0;
//                }
//            } else {
//                // 发送消息，但不再加入超时管理器，达到最大发送失败次数就算了
//                sendMsg();
//            }
//        }
//    }
//
//    public void sendMsg() {
//        System.out.println("正在重发消息，message=" + msg);
//        imsClient.sendMsg(msg, false);
//    }
//
//    public ImData getMsg() {
//        return msg;
//    }
//
//    @Override
//    public void cancel() {
//        if (task != null) {
//            task.cancel();
//            task = null;
//        }
//
//        super.cancel();
//    }
//}
