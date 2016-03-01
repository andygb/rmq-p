package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.Topic;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public interface TopicScanObserver {

    void seeTopic(Topic topic);
}
