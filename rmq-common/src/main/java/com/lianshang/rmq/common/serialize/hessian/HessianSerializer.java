package com.lianshang.rmq.common.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.AbstractSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yuan.zhong on 2016-02-01.
 *
 * @author yuan.zhong
 */
public class HessianSerializer implements AbstractSerializer {

    HessianSerializerFactory hessianSerializerFactory = new HessianSerializerFactory();

    @Override
    public void serialize(OutputStream os, Object obj) throws SerializationException {
        Hessian2Output h2out = new Hessian2Output(os);
        h2out.setSerializerFactory(hessianSerializerFactory);

        try {
            h2out.writeObject(obj);
        } catch (Throwable t) {
            throw new SerializationException(t);
        } finally {
            try {
                h2out.close();
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException {
        Hessian2Input h2in = new Hessian2Input(is);
        h2in.setSerializerFactory(hessianSerializerFactory);

        try {
            return (T) h2in.readObject(clazz);
        } catch (Throwable t) {
            throw new SerializationException(t);
        } finally {
            try {
                h2in.close();
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }
}
