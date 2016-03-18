package com.lianshang.rmq.admin.proxy;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.api.util.IdUtil;
import com.lianshang.rmq.monitor.job.TopicScanObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
public class TopicProxy {


    @Autowired
    TopicService topicService;

    List<TopicScanObserver> observerList;

    public void init() {
        if (observerList == null) {
            observerList = new ArrayList<>();
        }
    }

    public Integer add(Topic topic) {
        Integer topicId = topicService.add(topic);

        if (IdUtil.validId(topicId)) {
            for (TopicScanObserver observer : observerList) {
                observer.seeTopic(topic);
            }
        }

        return topicId;
    }

    public void setObserverList(List<TopicScanObserver> observerList) {
        this.observerList = observerList;
    }
}
