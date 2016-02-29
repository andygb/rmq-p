package com.lianshang.rmq.common.serialize.thrift;

import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.AbstractSerializer;
import com.lianshang.rmq.common.dto.MessageTrans;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-02-29.
 *
 * @author yuan.zhong
 */
public class ThriftSerializer implements AbstractSerializer {

    private static Map<Class, ThriftTBaseTranser> transerMap = new HashMap<>();

    private static final Object registerLock = new Object();

    @Override
    public void serialize(OutputStream os, Object obj) throws SerializationException {
        ThriftTBaseTranser transer = transerMap.get(obj.getClass());

        if (transer == null) {
            throw new SerializationException(String.format("Unsupported thrift base class %s",obj.getClass()));
        }

        try {
            TBase tBase = transer.trans(obj);
            tBase.write(new TBinaryProtocol(new TIOStreamTransport(os)));
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException {
        ThriftTBaseTranser transer = transerMap.get(clazz);

        if (transer == null) {
            throw new SerializationException(String.format("Unsupported thrift base class %s",clazz));
        }

        try {

            TBase tBase = transer.transInstance();

            tBase.read(new TBinaryProtocol(new TIOStreamTransport(is)));

            return (T)transer.detrans(tBase);

        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    public static <T> void registerTranser(Class<T> clazz, ThriftTBaseTranser<T, ?> transer) {
        synchronized (registerLock) {
            transerMap.put(clazz, transer);
        }
    }

}
