package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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

    List<TopicScanObserver> observerList;

    public TopicScanJob() {
        this.observerList = new ArrayList<>();
    }

    public TopicScanJob(List<TopicScanObserver> observerList) {
        this.observerList = observerList;
    }

    public void run() {
        int lastId = 0;
        int obeSize = observerList.size();
       ok: while (true) {//定义标签ok 方便循环跳出
             List<Topic> list = topicService.getByStep(lastId,STEP);
            if(list!=null && list.size()>0){
                for(Topic topics  : list){
                    if(obeSize>0 && observerList != null){
                        for(TopicScanObserver tso : observerList){
                            tso.seeTopic(topics);
                        }
                    }else{
                        looger.info("【****************topicscanObserver集合为空***********】");
                        break ok;
                    }

                }
            }else{
                looger.info("【**************没有查询到有topic***********】");
                break ok;
            }
            lastId +=STEP;
        }
    }
}
