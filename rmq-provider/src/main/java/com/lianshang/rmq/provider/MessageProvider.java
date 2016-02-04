package com.lianshang.rmq.provider;

import com.lianshang.common.utils.general.IpUtil;
import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.SerializeUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class MessageProvider {

    String topic;

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageProvider.class);

    public MessageProvider(String topic) {
        this.topic = topic;
    }

    public void send(Object content) throws SerializationException, ConnectionException {

        if (content == null) {
            throw new IllegalArgumentException("Message content could not be null!");
        }

        Channel channel = Connector.getChannel();

        byte[] contentBytes = SerializeUtils.serialize(content, SerializeUtils.getContentSerializer());

        Message message = new Message(IpUtil.getFirstNoLoopbackIP4Address(), new Date(), contentBytes);

        byte[] messageBytes = SerializeUtils.serialize(message, SerializeUtils.getMessageSerializer());
//        channel.exchangeDeclare(topic, "fanout");

        try {
            channel.basicPublish(topic, "", null, messageBytes);
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
