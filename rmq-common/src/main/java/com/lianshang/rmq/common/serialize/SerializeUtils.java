package com.lianshang.rmq.common.serialize;

import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.hessian.HessianSerializer;
import com.lianshang.rmq.common.serialize.jackson.JacksonSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by yuan.zhong on 2016-02-04.
 *
 * @author yuan.zhong
 */
public class SerializeUtils {

    private static final int BUFFER_SIZE = 1024;

    private static final AbstractSerializer contentSerializer = new JacksonSerializer();

    private static final AbstractSerializer messageSerializer = new HessianSerializer();

    public static AbstractSerializer getMessageSerializer() {
        return messageSerializer;
    }

    public static AbstractSerializer getContentSerializer() {
        return contentSerializer;
    }

    public static byte[] serialize(Object obj, AbstractSerializer serializer) throws SerializationException {

        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);

        serializer.serialize(os, obj);

        return os.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz, AbstractSerializer serializer) throws SerializationException {

        return serializer.deserialize(new ByteArrayInputStream(bytes), clazz);
    }
}
