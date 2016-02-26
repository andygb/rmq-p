package com.lianshang.rmq.demo;

import java.io.Serializable;

/**
 * Created by yuan.zhong on 2016-02-26.
 *
 * @author yuan.zhong
 */
public class TestClass implements Serializable {

    String content;

    public TestClass() {
    }

    public TestClass(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
