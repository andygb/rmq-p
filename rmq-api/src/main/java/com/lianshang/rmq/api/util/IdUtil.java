package com.lianshang.rmq.api.util;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
public class IdUtil {

    public static boolean validId(Integer id) {
        return !(id == null || id == 0);

    }
}
