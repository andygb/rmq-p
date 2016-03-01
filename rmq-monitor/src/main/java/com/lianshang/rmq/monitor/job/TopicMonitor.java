package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.consumer.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public class TopicMonitor implements TopicScanObserver {

    final private static Logger LOGGER = LoggerFactory.getLogger(TopicMonitor.class);

    Map<String, MessageListener> listenerMap = new HashMap<>();

    @Override
    public void seeTopic(Topic topic) {
        // TODO do something
    }

    public void close() {
        for (MessageListener listener : listenerMap.values()) {
            try {
                listener.close();
            } catch (ConnectionException e) {
                LOGGER.error("listener close exception", e);
            }
        }
    }
}
