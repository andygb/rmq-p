package com.lianshang.rmq.common.serialize.thrift;

import org.apache.thrift.TBase;

/**
 * Created by yuan.zhong on 2016-02-29.
 *
 * @author yuan.zhong
 */
public interface ThriftTBaseTranser <O, T extends TBase<T, ?>> {

    T trans(O origin);

    O detrans(T trans);

    T transInstance();
}
