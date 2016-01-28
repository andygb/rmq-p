package com.lianshang.rmq.consumer;

import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.ConstantDef;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class MessageListener {

    String topic;

    String consumerId;

    MessageConsumer consumer;

    public MessageListener(String topic, String consumerId, MessageConsumer consumer) throws IOException, TimeoutException {
        this.topic = topic;
        this.consumerId = consumerId;
        this.consumer = consumer;

        Connection connection = Connector.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(topic, "fanout");
        String queueName = String.format("%s%s%s", topic, ConstantDef.EXCHANGE_QUEUE_SEP, consumerId);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, topic, "");

        channel.basicConsume(queueName, true, new ApplyConsumer(channel, consumer));

    }

    class ApplyConsumer extends DefaultConsumer {

        MessageConsumer messageConsumer;

        public ApplyConsumer(Channel channel, MessageConsumer messageConsumer) {
            super(channel);
            this.messageConsumer = messageConsumer;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String content = new String(body);
            messageConsumer.onMessage(new Message(content));
        }
    }
}
