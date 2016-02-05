package com.lianshang.rmq.common.dto;

import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.SerializeUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class Message implements Serializable {

    final String producerIp;

    final Date birthTime;

    final byte[] content;

    public Message(String producerIp, Date birthTime, byte[] content) {
        this.producerIp = producerIp;
        this.birthTime = birthTime;
        this.content = content;
    }

    public String getProducerIp() {
        return producerIp;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public byte[] getContentBytes() {
        return content;
    }

    public String getContentString() throws SerializationException {
        return new String(content);
//        return SerializeUtils.deserialize(content, String.class, SerializeUtils.getContentSerializer());
    }

    public <T> T getContentBean(Class<T> clazz) throws SerializationException {

        if (clazz == String.class) {
            return (T) getContentString();
        }

        return SerializeUtils.deserialize(content, clazz, SerializeUtils.getContentSerializer());
    }
}
