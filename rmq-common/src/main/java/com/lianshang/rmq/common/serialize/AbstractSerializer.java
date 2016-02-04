package com.lianshang.rmq.common.serialize;

import com.lianshang.rmq.common.exception.SerializationException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yuan.zhong on 2016-02-01.
 *
 * @author yuan.zhong
 */
public interface AbstractSerializer {

    public void serialize(OutputStream os, Object obj) throws SerializationException;

    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException;
}
