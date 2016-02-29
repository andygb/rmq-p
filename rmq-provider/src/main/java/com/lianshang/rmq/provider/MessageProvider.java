package com.lianshang.rmq.provider;

import com.dianping.cat.Cat;
import com.dianping.lion.EnvZooKeeperConfig;
import com.lianshang.common.utils.general.IdGenerator;
import com.lianshang.common.utils.general.IpUtil;
import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * 消息发布者
 *
 * @author yuan.zhong
 */
public class MessageProvider {

    String topic;

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageProvider.class);

    private static final int machineIdx = EnvZooKeeperConfig.getMachineId();



    /**
     * 构造消息发布者
     * @param topic 消息主题
     */
    public MessageProvider(String topic) {
        this.topic = topic;
    }

    /**
     * 发送二进制消息
     * @param content   消息内容
     * @throws ConnectionException
     * @throws SerializationException
     */
    public void sendBytes(byte[] content) throws ConnectionException, SerializationException {
        if (content == null) {
            throw new IllegalArgumentException("Message content could not be null!");
        }

        sendContentBytes(content);
    }

    /**
     * 发送字符串消息
     * @param content   消息内容
     * @throws ConnectionException
     * @throws SerializationException
     */
    public void sendString(String content) throws ConnectionException, SerializationException {
        if (content == null) {
            throw new IllegalArgumentException("Message content could not be null!");
        }

        sendContentBytes(content.getBytes());
    }

    /**
     * 发送对象消息
     * @param content   消息内容，需要是实现Serializable的POJO
     * @throws SerializationException
     * @throws ConnectionException
     */
    public <T extends Serializable> void sendObject(T content) throws SerializationException, ConnectionException {

        if (content == null) {
            throw new IllegalArgumentException("Message content could not be null!");
        }

        if (content instanceof String) {
            sendString((String) content);
            return;
        }

        if (content instanceof byte[]) {
            sendContentBytes((byte[]) content);
            return;
        }

        byte[] contentBytes = SerializeUtils.serialize(content, SerializeUtils.getContentSerializer());

        sendContentBytes(contentBytes);

        Cat.newEvent("RMQ-Send", topic);
    }

    private void sendContentBytes(byte[] content) throws SerializationException, ConnectionException {
        Message message = new Message(IdGenerator.generateRmqId(), IpUtil.getFirstNoLoopbackIP4Address(), new Date(), content);

        byte[] messageBytes = SerializeUtils.serialize(message, SerializeUtils.getMessageSerializer());

        try {
            Connector.getChannel().basicPublish(topic, "", null, messageBytes);
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }



    public void close() throws ConnectionException {
        Connector.close();
    }

    public String getTopic() {
        return topic;
    }
}
