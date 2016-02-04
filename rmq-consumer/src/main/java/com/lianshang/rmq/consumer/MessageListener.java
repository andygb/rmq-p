package com.lianshang.rmq.consumer;

import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.ConstantDef;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.SerializeUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    String topic;

    String consumerId;

    MessageConsumer consumer;

    Channel channel;

    public MessageListener(String topic, String consumerId, MessageConsumer consumer) throws ConnectionException {
        this.topic = topic;
        this.consumerId = consumerId;
        this.consumer = consumer;


        channel = Connector.getChannel();

        String queueName = String.format("%s%s%s", topic, ConstantDef.EXCHANGE_QUEUE_SEP, consumerId);

        try {
            channel.queueDeclare(queueName, false, false, false, null);

            channel.queueBind(queueName, topic, "");

            channel.basicConsume(queueName, false, new ApplyConsumer(channel, consumer));
        } catch (IOException e) {
            throw new ConnectionException(e);
        }

    }

    public void close() throws ConnectionException {
         Connector.close();
    }

    class ApplyConsumer extends DefaultConsumer {

        MessageConsumer messageConsumer;

        public ApplyConsumer(Channel channel, MessageConsumer messageConsumer) {
            super(channel);
            this.messageConsumer = messageConsumer;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

            Message message;
            try {
                message = SerializeUtils.deserialize(body, Message.class,  SerializeUtils.getMessageSerializer());
                ConsumeResult result = messageConsumer.onMessage(message);

                switch (result.action) {
                    case REJECT:
                        channel.basicNack(envelope.getDeliveryTag(), false, false);
                        break;
                    case RETRY:
                        if (envelope.isRedeliver()) {
                            // 已是重试消息
                            channel.basicNack(envelope.getDeliveryTag(), false, false);
                            LOGGER.error("Message tag {} consume retry again", envelope.getDeliveryTag());
                        } else {
                            channel.basicNack(envelope.getDeliveryTag(), false, true);
                        }
                        break;
                    case ACCEPT:
                    default:
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        break;
                }
            } catch (Exception e) {
                LOGGER.error("Consume error, message tag {}", envelope.getDeliveryTag(), e);
                throw new IOException(e);
            }

        }

    }
}
