package com.lianshang.rmq.biz.query;

import java.util.Date;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
public class MessageRecordQueryEntity {

    String topic;

    Long birthTimeBegin;

    Long birthTimeEnd;

    String producerIp;


    public String getTopic() {
        return topic;
    }

    public MessageRecordQueryEntity setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Long getBirthTimeBegin() {
        return birthTimeBegin;
    }

    public MessageRecordQueryEntity setBirthTimeBegin(Long birthTimeBegin) {
        this.birthTimeBegin = birthTimeBegin;
        return this;
    }


    public Long getBirthTimeEnd() {
        return birthTimeEnd;
    }

    public MessageRecordQueryEntity setBirthTimeEnd(Long birthTimeEnd) {
        this.birthTimeEnd = birthTimeEnd;
        return this;
    }

    public String getProducerIp() {
        return producerIp;
    }

    public MessageRecordQueryEntity setProducerIp(String producerIp) {
        this.producerIp = producerIp;
        return this;
    }
}
