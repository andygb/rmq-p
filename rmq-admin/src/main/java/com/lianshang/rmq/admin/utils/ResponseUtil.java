package com.lianshang.rmq.admin.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by Bao on 2016-03-02.
 */
public class ResponseUtil {

    public static final int SUCCESS_CODE=200;

    public static Map<String, Object> wrapByAuthFail() {
        return ImmutableMap.<String, Object>builder()
                .put("code", 997)
                .put("message", "账号异常")
                .put("runEnv", ClientConfig.getRunEnv())
                .put("clientVersion", ClientConfig.getClientVersion())
                .build();
    }

    public static Map<String, Object> wrap(Exception ex,String msg) {
        return ImmutableMap.<String, Object>builder()
                .put("code", 998)
                .put("message", msg+" 异常信息:"+ex.getMessage())
                .put("runEnv", ClientConfig.getRunEnv())
                .put("clientVersion", ClientConfig.getClientVersion())
                .build();
    }

    public static Map<String, Object> wrap(int code, String message) {
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("message", message)
                .put("runEnv", ClientConfig.getRunEnv())
                .put("clientVersion", ClientConfig.getClientVersion())
                .build();
    }

    public static Map<String, Object> wrap(int code, String message, Map<String, Object> data) {
        return ImmutableMap.<String, Object>builder()
                .put("code", code)
                .put("message", message)
                .put("runEnv", ClientConfig.getRunEnv())
                .put("clientVersion", ClientConfig.getClientVersion())
                .put("data", data)
                .build();
    }

}
