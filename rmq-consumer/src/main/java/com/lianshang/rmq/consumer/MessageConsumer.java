package com.lianshang.rmq.consumer;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public interface MessageConsumer {

    void onMessage(Message message);
}
