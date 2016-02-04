package com.lianshang.rmq.consumer;

/**
 * Created by yuan.zhong on 2016-02-04.
 *
 * @author yuan.zhong
 */
public enum ConsumeAction {

    ACCEPT,     // 处理成功
    REJECT,     // 拒绝，不重试
    RETRY       // 重试
}
