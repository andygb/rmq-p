package com.lianshang.rmq.consumer;

import com.lianshang.rmq.common.dto.Message;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public interface MessageConsumer {

    ConsumeResult onMessage(Message message);
}
