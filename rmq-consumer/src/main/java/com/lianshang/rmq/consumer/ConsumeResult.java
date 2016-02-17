package com.lianshang.rmq.consumer;

/**
 * Created by yuan.zhong on 2016-02-04.
 *
 * @author yuan.zhong
 */
public class ConsumeResult {

    final ConsumeAction action;

    final String message;

    /**
     * 构造消息处理结果
     * @param action    消费行为
     *                  @see com.lianshang.rmq.consumer.ConsumeAction
     * @param message   备注信息
     */
    public ConsumeResult(ConsumeAction action, String message) {
        this.action = action;
        this.message = message;
    }

    /**
     * 构造消息处理结果，默认处理成功
     * @param message   备注信息
     */
    public ConsumeResult(String message) {
        this.action = ConsumeAction.ACCEPT;
        this.message = message;
    }

    public ConsumeAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

}
