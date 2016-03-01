package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.consumer.MessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public class TopicMonitor implements TopicScanObserver {

    Map<String, MessageListener> listenerMap = new HashMap<>();

    @Override
    public void seeTopic(Topic topic) {
        // TODO do something
    }
}
