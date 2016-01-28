package com.lianshang.rmq.consumer;

/**
 * Created by yuan.zhong on 2016-01-27.
 *
 * @author yuan.zhong
 */
public class Message  {

    String content;

    public String getContent() {
        return content;
    }

    public Message(String content) {
        this.content = content;
    }
}
