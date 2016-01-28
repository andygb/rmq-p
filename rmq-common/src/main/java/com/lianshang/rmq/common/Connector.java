package com.lianshang.rmq.common;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-28.
 *
 * @author yuan.zhong
 */
public class Connector {

    static Connection connection;

    public static Connection getConnection() throws IOException, TimeoutException {
        if (connection == null ) {
            synchronized (Connector.class) {
                if (connection == null) {
                    ConnectionFactory factory = new ConnectionFactory();

                    factory.setHost("192.168.1.20");
                    factory.setPort(5672);
                    factory.setUsername("root");
                    factory.setPassword("root");

                    connection = factory.newConnection();
                }
            }
        }
        return connection;
    }
}
