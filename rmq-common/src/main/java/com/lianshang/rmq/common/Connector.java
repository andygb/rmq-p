package com.lianshang.rmq.common;

import com.lianshang.common.utils.general.LionUtil;
import com.lianshang.common.utils.general.StringUtil;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by yuan.zhong on 2016-01-28.
 *
 * @author yuan.zhong
 */
public class Connector {

    private final static String ADRESS_CONFIG_KEY = "rabbitmq.connection.addresses";
    private final static String USERNAME_CONFIG_KEY = "rabbitmq.connection.username";
    private final static String PASSWORD_CONFIG_KEY = "rabbitmq.connection.password";

    private final static Logger LOGGER = LoggerFactory.getLogger(Connector.class);

    static Connection connection;

    static ChannelThreadLocal channelThreadLocal = new ChannelThreadLocal();

    public static Channel getChannel() throws ConnectionException {

        Channel channel = channelThreadLocal.get();


        if (!isValidChannel(channel)) {
            try {
                channel = getConnection().createChannel();
            } catch (IOException e) {
                throw new ConnectionException(e);
            }
            channelThreadLocal.set(channel);
        }

        return channel;
    }

    public static void close() throws ConnectionException {
        if (isValidConnection(connection)) {
            synchronized (Connector.class) {
                if (isValidConnection(connection)) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        throw new ConnectionException(e);
                    }
                }
            }
        }
    }

    private static Connection getConnection() throws ConnectionException {
        if (!isValidConnection(connection)) {
            synchronized (Connector.class) {
                if (!isValidConnection(connection)) {
                    connection = newConnection();
                }
            }
        }
        return connection;
    }

    private static boolean isValidConnection(Connection connection) {
        return (connection != null && connection.isOpen());
    }

    private static boolean isValidChannel(Channel channel) {
        return (channel != null && channel.isOpen());
    }

    private static Connection newConnection() throws ConnectionException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername(LionUtil.getString(USERNAME_CONFIG_KEY));
        factory.setPassword(LionUtil.getString(PASSWORD_CONFIG_KEY));
        Address[] addresses = getBrokerAddresses();
        if (addresses == null || addresses.length == 0) {
            throw new ConnectionException("No available broker");
        }

        factory.setHost(addresses[0].getHost());
        factory.setPort(addresses[0].getPort());

        try {
            return factory.newConnection(addresses);
        } catch (IOException | TimeoutException e) {
            throw new ConnectionException(e);
        }
    }

    private static Address[] getBrokerAddresses() {

        String config = LionUtil.getString(ADRESS_CONFIG_KEY);

        String[] addressConfigs = config.split(",");

        List<Address> addressList = new ArrayList<>();
        for (String addressConfig : addressConfigs) {

            try {
                String[] segs = addressConfig.split(":");
                String ip = segs[0];
                int port = StringUtil.parseInt(segs[1]);

                addressList.add(new Address(ip, port));
            } catch (Throwable e) {
                LOGGER.error("Address config error, config {} : ", addressConfig, e);
            }

        }

        return addressList.toArray(new Address[addressList.size()]);
    }

    static class ChannelThreadLocal {

        static Map<Thread, Channel> threadChannelMap = new ConcurrentHashMap<>();

        public Channel get() {
            clear();
            Thread thread = Thread.currentThread();
            return threadChannelMap.get(thread);
        }

        public void set(Channel channel) {
            Thread thread = Thread.currentThread();
            threadChannelMap.put(thread, channel);
        }

        private void clear() {
            for (Map.Entry<Thread, Channel> entry : threadChannelMap.entrySet()) {
                Thread thread = entry.getKey();
                Channel channel = entry.getValue();
                if (thread == null || !thread.isAlive()) {
                    threadChannelMap.remove(thread);
                    try {
                        if (isValidChannel(channel)) {
                            channel.close();
                        }
                    } catch (com.rabbitmq.client.AlreadyClosedException ignored) {
                    } catch (Throwable e) {
                        LOGGER.error("Channel clear error : ",e);
                    }
                }
            }
        }


    }
}
