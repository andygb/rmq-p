package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public class TopicScanJob {

    final private static Logger looger = LoggerFactory.getLogger(TopicScanJob.class);

    @Autowired
    TopicService topicService;

    final static int STEP = 100;
    final static long INTERVAL = 1000 * 60 * 5;

    List<TopicScanObserver> observerList;

    private Timer timer = null;

    public void init() {
        if (this.observerList == null) {
            this.observerList = new ArrayList<>();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doWork();
            }
        }, 1, INTERVAL);
    }

    public void destroy() {
        timer.cancel();
    }

    public void doWork() {
        int lastId = 0;
        while (true) {
            List<Topic> list = topicService.getByStep(lastId,STEP);

            if (CollectionUtils.isEmpty(list)) {
                break;
            }

            for(Topic topic  : list){
                lastId = Math.max(lastId, topic.getId());
                for(TopicScanObserver tso : observerList){
                    try {
                        tso.seeTopic(topic);
                    } catch (Throwable e) {
                        looger.error("Topic process error!", e);
                    }
                }
            }
        }

//        looger.info("【*********一共调用了{}次**********】",++counts);
    }

    public void setObserverList(List<TopicScanObserver> observerList) {
        this.observerList = observerList;
    }
}
