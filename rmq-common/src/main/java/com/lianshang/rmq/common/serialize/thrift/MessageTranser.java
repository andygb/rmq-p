package com.lianshang.rmq.common.serialize.thrift;

import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.dto.MessageTrans;
import org.apache.thrift.TBase;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by yuan.zhong on 2016-02-29.
 *
 * @author yuan.zhong
 */
public class MessageTranser implements ThriftTBaseTranser<Message, MessageTrans> {
    @Override
    public MessageTrans trans(Message origin) {
        return new MessageTrans(origin.getId(), origin.getProducerIp(), origin.getBirthTime().getTime(), ByteBuffer.wrap(origin.getContentBytes()));
    }

    @Override
    public Message detrans(MessageTrans trans) {
        return new Message(trans.getId(), trans.getProducerIp(), new Date(trans.getBirthTime()), trans.getContent());
    }

    @Override
    public MessageTrans transInstance() {
        return new MessageTrans();
    }


}
