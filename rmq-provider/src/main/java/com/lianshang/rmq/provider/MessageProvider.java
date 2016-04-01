package com.lianshang.rmq.provider;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
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

        Transaction transaction = Cat.newTransaction("RMQ.Produce", topic);
        try {
            sendContentBytes(content, transaction);
            transaction.setStatus(Transaction.SUCCESS);
        } catch (ConnectionException | SerializationException e) {
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
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

        Transaction transaction = Cat.newTransaction("RMQ.Produce", topic);
        try {
            sendContentBytes(content.getBytes(), transaction);
            transaction.setStatus(Transaction.SUCCESS);
        } catch (ConnectionException | SerializationException e) {
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
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
            sendBytes((byte[]) content);
            return;
        }

        Transaction transaction = Cat.newTransaction("RMQ.Produce", topic);

        try {
            byte[] contentBytes = SerializeUtils.serialize(content, SerializeUtils.getContentSerializer());
            Event objectSerializationEvent = Cat.newEvent("RMQ.Product.ObjSerialization", topic);
            transaction.addChild(objectSerializationEvent);
            sendContentBytes(contentBytes, transaction);
            transaction.setStatus(Transaction.SUCCESS);
        } catch (ConnectionException | SerializationException e) {
            transaction.setStatus(e);
        } finally {
            transaction.complete();
        }
    }

    private void sendContentBytes(byte[] content, Transaction transaction) throws ConnectionException, SerializationException {

        try {


            Message message = new Message(IdGenerator.generateRmqId(), IpUtil.getFirstNoLoopbackIP4Address(), new Date(), content);
            transaction.addData("messageId", message.getId());

            byte[] messageBytes = SerializeUtils.serialize(message, SerializeUtils.getMessageSerializer());

            Event serializationEvent = Cat.newEvent("RMQ.Produce.Serialization", topic);
            transaction.addChild(serializationEvent);

            try {
                Connector.getChannel().basicPublish(topic, "", null, messageBytes);
                Event publishEvent = Cat.newEvent("RMQ.Produce.Publish", topic);
                transaction.addChild(publishEvent);
            } catch (IOException e) {
                throw new ConnectionException(e);
            }

        } catch (ConnectionException | SerializationException e) {
            throw e;
        }

    }



    public void close() throws ConnectionException {
        Connector.close();
    }

    public String getTopic() {
        return topic;
    }
}
