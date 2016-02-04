package com.lianshang.rmq.demo;

import com.lianshang.common.utils.general.StringUtil;
import com.lianshang.rmq.common.Connector;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-02-04.
 *
 * @author yuan.zhong
 */
public class LocalChannelTest {

    public static void main(String[] args) throws ConnectionException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();

            if (StringUtil.isEmpty(line)) {
                continue;
            }

            int sleep = StringUtil.parseInt(line);

            if (sleep <= 0) {
                break;
            }

            new Thread(new TestLocalChannelThread(sleep)).start();
        }

        Connector.close();
    }

    public static class TestLocalChannelThread implements Runnable {
        int sleep;

        public TestLocalChannelThread(int sleep) {
            this.sleep = sleep;
        }

        @Override
        public void run() {
            try {
                final long tid = Thread.currentThread().getId();
                System.out.println(String.format("t %d start", tid));

                final Channel channel = Connector.getChannel();

                channel.addShutdownListener(new ShutdownListener() {
                    @Override
                    public void shutdownCompleted(ShutdownSignalException e) {
                        System.out.println(String.format("t %d channel closed", tid));
                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();
                        channel.close();
                    }
                });

                System.out.println(String.format("Got channel status %s", channel.isOpen()));

                Thread.sleep(sleep);

                System.out.println(String.format("t %d end", tid));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
