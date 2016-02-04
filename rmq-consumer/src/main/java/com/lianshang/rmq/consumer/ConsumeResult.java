package com.lianshang.rmq.consumer;

/**
 * Created by yuan.zhong on 2016-02-04.
 *
 * @author yuan.zhong
 */
public class ConsumeResult {

    final ConsumeAction action;

    final String message;

    public ConsumeResult(ConsumeAction action, String message) {
        this.action = action;
        this.message = message;
    }

    public ConsumeAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

}
