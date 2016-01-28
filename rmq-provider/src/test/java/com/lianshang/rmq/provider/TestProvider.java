package com.lianshang.rmq.provider;

import org.junit.Test;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class TestProvider {

    public static void main(String[] args) throws IOException, TimeoutException {

        Scanner scanner = new Scanner(System.in);

        String[] topics = {"topic1", "topic2"};

        List<MessageProvider> providers = new ArrayList<>();

        for (String topic : topics) {
            providers.add(new MessageProvider(topic));
        }

        Random random = new Random();

        while (true) {

            int index = random.nextInt(providers.size());

            MessageProvider provider = providers.get(index);

            System.out.printf("Input message (%s): ", provider.getTopic());

            String input = scanner.nextLine();

            if ("quit".equals(input)) {
                break;
            }

            provider.send(input);
        }

        for (MessageProvider provider : providers) {
            provider.close();
        }
    }
}
