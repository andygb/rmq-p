package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public class TopicScanJob {

    @Autowired
    TopicService topicService;

    final static int STEP = 100;

    List<TopicScanObserver> observerList;

    public TopicScanJob() {
    }

    public TopicScanJob(List<TopicScanObserver> observerList) {
        this.observerList = observerList;
    }

    public void run() {
        int lastId = 0;

        while (true) {
            // TODO do something
        }
    }
}
