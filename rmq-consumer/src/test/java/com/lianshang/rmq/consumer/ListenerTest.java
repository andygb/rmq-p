package com.lianshang.rmq.consumer;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class ListenerTest {

    @Test
    public void testListener() throws IOException, TimeoutException, InterruptedException {

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

    class TestConsumer implements MessageConsumer {

        String topic;

        String consumerId;

        int no;

        TestConsumer(String topic, String consumerId, int no) {
            this.topic = topic;
            this.consumerId = consumerId;
            this.no = no;
        }

        @Override
        public void onMessage(Message message) {
            System.out.println(String.format("msg received by {%s}, topic {%s}, no {%s}, content {%s}",
                     consumerId, topic, no, message.getContent()));
        }
    }
}
