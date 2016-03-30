package com.lianshang.rmq.consumer;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultEvent;
import com.lianshang.common.utils.general.IpUtil;
import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.ConstantDef;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
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
 * 消息监听器
 *
 * @author yuan.zhong
 */
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    String topic;

    String consumerId;

    MessageConsumer consumer;

    ApplyConsumer applyConsumer;

    Channel channel;

    /**
     * 构造监听器
     * @param topic 消息主题
     * @param consumerId    消费者ID
     * @param consumer      消费者，即消息处理器
     * @throws ConnectionException
     */
    public MessageListener(String topic, String consumerId, MessageConsumer consumer) throws ConnectionException {
        this.topic = topic;
        this.consumerId = consumerId;
        this.consumer = consumer;



        String queueName = String.format("%s%s%s", topic, ConstantDef.EXCHANGE_QUEUE_SEP, consumerId);

        try {

            channel = Connector.getConnection().createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            channel.queueBind(queueName, topic, "");

            channel.basicConsume(queueName, false, new ApplyConsumer(channel, consumer));
        } catch (IOException e) {
            throw new ConnectionException(e);
        }

    }

    public void close() throws ConnectionException {
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new ConnectionException(e);
        }
    }

    class ApplyConsumer extends DefaultConsumer {

        MessageConsumer messageConsumer;

        public ApplyConsumer(Channel channel, MessageConsumer messageConsumer) {
            super(channel);
            this.messageConsumer = messageConsumer;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            Transaction transaction = Cat.newTransaction("RMQ.Consume", topic);

            try {
                // Deserialization
                Event deserializationEvent = Cat.newEvent("RMQ.Consume.Deserialization", topic);
                transaction.addChild(deserializationEvent);
                Message message = SerializeUtils.deserialize(body, Message.class,  SerializeUtils.getMessageSerializer());
                deserializationEvent.addData("messageId", message.getId());
                deserializationEvent.addData("retry", envelope.isRedeliver() ? 1 : 0);
                deserializationEvent.complete();

                // Process
                Event processEvent = Cat.newEvent("RMQ.Consume.Process", topic);
                transaction.addChild(processEvent);
                processEvent.addData("messageId", message.getId());
                processEvent.addData("consumerId", consumerId);
                processEvent.addData("consumerIp", IpUtil.getFirstNoLoopbackIP4Address());
                ConsumeResult result = messageConsumer.onMessage(message, topic);
                processEvent.complete();

                // ACK
                Event ackEvent = Cat.newEvent("RMQ.Consume.ACK", topic);
                transaction.addChild(ackEvent);
                ackEvent.addData("messageId", message.getId());
                switch (result.action) {
                    case REJECT:
                        getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                        ackEvent.addData("action", "REJECT");
                        break;
                    case RETRY:
                        if (envelope.isRedeliver()) {
                            // 已是重试消息
//                            Connector.getChannel().basicAck(envelope.getDeliveryTag(), false);
                            getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                            LOGGER.error("Message tag {} consume retry again", envelope.getDeliveryTag());
                        } else {
                            getChannel().basicNack(envelope.getDeliveryTag(), false, true);
                        }
                        ackEvent.addData("action", "RETRY");
                        break;
                    case ACCEPT:
                    default:
                        getChannel().basicAck(envelope.getDeliveryTag(), false);
                        ackEvent.addData("action", "ACCEPT");
                        break;
                }
                ackEvent.complete();

                transaction.setStatus(Transaction.SUCCESS);
            } catch (Throwable e) {
                // 发生异常，重试
                LOGGER.error("Consume error, message tag {}", envelope.getDeliveryTag(), e);
                if (envelope.isRedeliver()) {
                    getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                } else {
                    getChannel().basicNack(envelope.getDeliveryTag(), false, true);
                }
                transaction.setStatus(e);
            }

            transaction.complete();
        }

    }
}
