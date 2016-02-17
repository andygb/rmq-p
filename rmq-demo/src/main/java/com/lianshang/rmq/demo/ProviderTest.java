package com.lianshang.rmq.demo;

import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.provider.MessageProvider;

import java.util.*;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class ProviderTest {

    public static void main(String[] args) throws InterruptedException, SerializationException, ConnectionException {

//        int length = Integer.valueOf(args[0]);

//        effiencyTest(length);
        functionalTest();
    }

//    @Test
    public static void functionalTest() throws SerializationException, ConnectionException {

        MessageProvider messageProvider = new MessageProvider("test-topic");

        messageProvider.sendString("An example message");

        messageProvider.close();


        Scanner scanner = new Scanner(System.in);

        String[] topics = {"topic1", "topic2"};

        List<MessageProvider> providers = new ArrayList<>();

        for (String topic : topics) {
            providers.add(new MessageProvider(topic));
        }

        Random random = new Random();

        int id = 0;
        while (true) {

            int index = random.nextInt(providers.size());

            MessageProvider provider = providers.get(index);

            System.out.printf("Input message (%s): ", provider.getTopic());

            String input = scanner.nextLine();

            Map<String, Object> content = new TreeMap<>();
            content.put("id", id++);
            content.put("msg", input);

            if ("quit".equals(input)) {
                break;
            }

            provider.sendBytes(input.getBytes());
//            provider.sendObject(boxBytes(input.getBytes()));
        }

        for (MessageProvider provider : providers) {
            provider.close();
        }
    }

    public static void effiencyTest(int length) throws InterruptedException, SerializationException, ConnectionException {
        String topic = "efficiency";

        MessageProvider provider = new MessageProvider(topic);

        int threadNum = 1;
        int loop = 20000;

//        int length = 1024 * 512;

        String content = getStringBySize(length);
        List<TestMessageSendThread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            TestMessageSendThread thread = new TestMessageSendThread(loop, content, provider);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        provider.sendString("quit");
        provider.close();

        for (int i = 0; i < threads.size(); i++) {
            TestMessageSendThread thread = threads.get(i);

            long ms = thread.getEndTime() - thread.getBeginTime();

            System.out.println(String.format("Provider %d finished during %d ms", i, ms));
        }
    }

    private static Byte[] boxBytes(byte[] bytes) {
        Byte[] boxBytes = new Byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            boxBytes[i] = bytes[i];
        }

        return boxBytes;
    }

    static class TestMessageSendThread extends Thread {

        int loop;

        String content;

        MessageProvider provider;

        long beginTime;

        long endTime;

        TestMessageSendThread(int loop, String content, MessageProvider provider) {
            this.loop = loop;
            this.content = content;
            this.provider = provider;
        }

        @Override
        public void run() {
            beginTime = System.currentTimeMillis();
            for (int i = 0; i < loop; i++) {
                try {
                    provider.sendString(content);
                } catch (SerializationException | ConnectionException e) {
                    e.printStackTrace();
                }
            }

            endTime = System.currentTimeMillis();
        }

        public long getBeginTime() {
            return beginTime;
        }

        public long getEndTime() {
            return endTime;
        }
    }

    private static String getStringBySize(int size) {
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append((char)('A' + random.nextInt(26)));
        }
        return sb.toString();
    }
}
