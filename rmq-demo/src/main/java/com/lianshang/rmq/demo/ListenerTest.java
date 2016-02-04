package com.lianshang.rmq.demo;


import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.consumer.ConsumeAction;
import com.lianshang.rmq.consumer.ConsumeResult;
import com.lianshang.rmq.consumer.MessageConsumer;
import com.lianshang.rmq.consumer.MessageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class ListenerTest {

    public static boolean running = true;

    public static void main(String[] args) throws InterruptedException, ConnectionException {
//        efficiencyTest();
        functionTest();
    }

//    @Test
    public static void functionTest() throws InterruptedException, ConnectionException {

        String[] topics = {"topic1", "topic2"};
        String[] consumerIds = {"A", "B"};

        List<MessageListener> listeners = new ArrayList<>();
        for (String topic : topics) {

            for (int i = 0; i < 2; i++) {
                for (String consumerId : consumerIds) {
                    listeners.add(new MessageListener(topic, consumerId, new TestConsumer(topic, consumerId, i)));
                }
            }
        }

        System.out.println("listener start");

        while (true) {
            Thread.sleep(1000);
        }
    }

    public static void efficiencyTest() throws InterruptedException, ConnectionException {
        String topic = "efficiency";
        String consumerId = "C";

        int listenerNum = 1;

        List<TestEfficiencyConsumer> consumers = new ArrayList<>();
        List<MessageListener> listeners = new ArrayList<>();
        for (int i = 0; i < listenerNum; i++) {
            TestEfficiencyConsumer consumer = new TestEfficiencyConsumer();
            consumers.add(consumer);
            listeners.add(new MessageListener(topic, consumerId, consumer));
        }

        while (running) {
            Thread.sleep(1000);
        }


        int totalMessageCount = 0;
        int totalByteCount = 0;

        long minBegin = Long.MAX_VALUE;

        long endTime = System.currentTimeMillis();

        for (MessageListener listener : listeners) {
            listener.close();
        }

        for (int i = 0; i < consumers.size(); i++) {
            TestEfficiencyConsumer consumer = consumers.get(i);

            if (consumer.getMessageCount() == 0) {
                continue;
            }

            long ms = endTime - consumer.getFirstTime();
            minBegin = Math.min(consumer.getFirstTime(), minBegin);

            System.out.println(String.format("Consumer no %d, received %d messages, total size %d during %d ms", i, consumer.getMessageCount(), consumer.getByteCount(),ms));
            totalMessageCount += consumer.getMessageCount();
            totalByteCount += consumer.getByteCount();
        }

        long ms = endTime - minBegin;
        System.out.println(String.format("Total message %d, byte %d received during %d ms", totalMessageCount, totalByteCount, ms));
    }

    static class TestConsumer implements MessageConsumer {

        String topic;

        String consumerId;

        int no;

        TestConsumer(String topic, String consumerId, int no) {
            this.topic = topic;
            this.consumerId = consumerId;
            this.no = no;
        }

        @Override
        public ConsumeResult onMessage(Message message)  {
            String contentString = null;
            try {
                contentString = message.getContentString();
            } catch (SerializationException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("msg received by {%s}, topic {%s}, no {%s}, content {%s}",
                     consumerId, topic, no, contentString));

            if ("re".equals(contentString)) {
                return new ConsumeResult(ConsumeAction.RETRY, "RE");
            }
            return new ConsumeResult("OK");
        }
    }

    static class TestEfficiencyConsumer implements MessageConsumer {

        int messageCount;

        int byteCount;

        long firstTime;

        long lastTime;

        @Override
        public ConsumeResult onMessage(Message message) {
            if (running) {
                try {
                    update(message.getContentString());
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            }
            return new ConsumeResult("OK");
        }

        synchronized private void update (String content) {
            messageCount ++;
            byteCount += content.getBytes().length;
            if (firstTime == 0) {
                firstTime = System.currentTimeMillis();
            }

            if ("quit".equalsIgnoreCase(content)) {
                running = false;
                lastTime = System.currentTimeMillis();
            }
        }

        public int getMessageCount() {
            return messageCount;
        }

        public int getByteCount() {
            return byteCount;
        }

        public long getFirstTime() {
            return firstTime;
        }

        public long getLastTime() {
            return lastTime;
        }
    }
}
