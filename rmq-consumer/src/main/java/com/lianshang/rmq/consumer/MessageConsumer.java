package com.lianshang.rmq.consumer;

import com.lianshang.rmq.common.dto.Message;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public interface MessageConsumer {

    /**
     * 处理消息
     * @param message 消息体
     * @return  消息处理结果
     */
    ConsumeResult onMessage(Message message, String topic);
}
