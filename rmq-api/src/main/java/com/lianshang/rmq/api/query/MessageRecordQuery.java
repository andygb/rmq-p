package com.lianshang.rmq.api.query;

import java.util.Date;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
public class MessageRecordQuery {

    String topic;

    Date birthTimeBegin;

    Date birthTimeEnd;

    String producerIp;


    public String getTopic() {
        return topic;
    }

    public MessageRecordQuery setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Date getBirthTimeBegin() {
        return birthTimeBegin;
    }

    public MessageRecordQuery setBirthTimeBegin(Date birthTimeBegin) {
        this.birthTimeBegin = birthTimeBegin;
        return this;
    }


    public Date getBirthTimeEnd() {
        return birthTimeEnd;
    }

    public MessageRecordQuery setBirthTimeEnd(Date birthTimeEnd) {
        this.birthTimeEnd = birthTimeEnd;
        return this;
    }

    public String getProducerIp() {
        return producerIp;
    }

    public MessageRecordQuery setProducerIp(String producerIp) {
        this.producerIp = producerIp;
        return this;
    }
}
