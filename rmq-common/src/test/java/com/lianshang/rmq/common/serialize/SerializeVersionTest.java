package com.lianshang.rmq.common.serialize;

import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.hessian.HessianSerializer;
import com.lianshang.rmq.common.serialize.jackson.JacksonSerializer;
import com.lianshang.rmq.common.serialize.thrift.MessageTranser;
import com.lianshang.rmq.common.serialize.thrift.ThriftSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-02-29.
 *
 * @author yuan.zhong
 */
public class SerializeVersionTest {

    String filePath = "/Users/buwoyouwo/Downloads/serialization/";

    static {
        ThriftSerializer.registerTranser(Message.class, new MessageTranser());
    }

    @Test
    public void toFile() throws IOException, SerializationException {

        Message messageTrans  = getMessageTrans();

        for (Map.Entry<String, AbstractSerializer> entry : getSerializers().entrySet()) {
            String fileName = getFileName(entry.getKey());
            File file = new File(fileName);
            entry.getValue().serialize(new FileOutputStream(file), messageTrans);
        }
    }

    @Test
    public void fromFile() throws FileNotFoundException, SerializationException {
        Message messageTrans = getMessageTrans();

        for (Map.Entry<String, AbstractSerializer> entry : getSerializers().entrySet()) {
            String fileName = getFileName(entry.getKey());

            Message de = entry.getValue().deserialize(new FileInputStream(fileName), Message.class);

            Assert.assertTrue(equalMessage(messageTrans, de));
        }
    }

    private Map<String, AbstractSerializer> getSerializers() {
        Map<String, AbstractSerializer> serializerMap = new HashMap<>();

        serializerMap.put("hessian", new HessianSerializer());
//        serializerMap.put("jackson", new JacksonSerializer());
        serializerMap.put("thrift", new ThriftSerializer());

        return serializerMap;
    }

    private String getFileName (String serialzerName) {
        return filePath + serialzerName;
    }

    private byte[] content = "ABC".getBytes();
    Message getMessageTrans() {
        return new Message(0, "127.0.0.1", new Date(0), content);
    }

    private boolean equalMessage(Message m1, Message m2) {
        if (m1 == m2){
            return true;
        }

        return m1.getId() == m2.getId()
                && Arrays.equals(m1.getContentBytes(), m2.getContentBytes())
                && m1.getBirthTime().equals(m2.getBirthTime())
                && m1.getProducerIp().equals(m2.getProducerIp());
    }
}
