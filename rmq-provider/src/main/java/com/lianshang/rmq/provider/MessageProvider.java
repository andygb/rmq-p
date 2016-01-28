package com.lianshang.rmq.provider;

import com.lianshang.rmq.common.Connector;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class MessageProvider {

    String topic;

    Connection connection;

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageProvider.class);

    private final static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public MessageProvider(String topic) throws IOException, TimeoutException {
        this.topic = topic;
        connection = Connector.getConnection();

    }

    public int send(String message) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(topic, "fanout");
        channel.basicPublish(topic, "", null, message.getBytes());

        channel.close();

        return 0;
    }

    public void close() throws IOException {
        connection.close();
    }

    public String getTopic() {
        return topic;
    }
}
