package com.lianshang.rmq.common.serialize.hessian;

import com.caucho.hessian.io.*;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuan.zhong on 2016-02-02.
 *
 * @author yuan.zhong
 */
public class HessianSerializerFactory extends SerializerFactory {

    private static ConcurrentHashMap serializerMap = new ConcurrentHashMap();
    private static ConcurrentHashMap deserializerMap = new ConcurrentHashMap();

    static {
        serializerMap.put(BigInteger.class, new StringValueSerializer());
        try {
            deserializerMap.put(BigInteger.class, new StringValueDeserializer(BigInteger.class));
        } catch (Throwable e) {
        }
    }

    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer = (Serializer) serializerMap.get(cl);
        if (serializer != null)
            return serializer;
        return super.getSerializer(cl);
    }

    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer = (Deserializer) deserializerMap.get(cl);
        if (deserializer != null)
            return deserializer;
        return super.getDeserializer(cl);
    }

}
