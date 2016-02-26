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

    /**
     * 获取发送方IP
     * @return
     */
    public String getProducerIp() {
        return producerIp;
    }

    /**
     * 获取消息产生的时间
     * @return
     */
    public Date getBirthTime() {
        return birthTime;
    }

    /**
     * 以二进制形式获取消息内容
     * @return
     */
    public byte[] getContentBytes() {
        return content;
    }

    /**
     * 以字符串形式获取消息内容
     * @return
     * @throws SerializationException
     */
    public String getContentString() throws SerializationException {
        return new String(content);
//        return SerializeUtils.deserialize(content, String.class, SerializeUtils.getContentSerializer());
    }

    /**
     * 反序列化获取消息内容
     * @param clazz 需为实现Serializable的POJO
     * @return
     * @throws SerializationException
     */
    public <T extends Serializable> T getContentBean(Class<T> clazz) throws SerializationException {

        if (clazz == String.class) {
            return (T) getContentString();
        }

        return SerializeUtils.deserialize(content, clazz, SerializeUtils.getContentSerializer());
    }
}
