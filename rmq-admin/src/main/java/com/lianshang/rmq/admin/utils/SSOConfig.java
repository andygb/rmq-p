package com.lianshang.rmq.admin.utils;

import com.lianshang.common.utils.general.LionUtil;

/**
 * Created by yuan.zhong on 2016-03-21.
 *
 * @author yuan.zhong
 */
public class SSOConfig {
    public static String getAppKey() {
        return LionUtil.getString("rabbitmq.sso.appKey");
    }

    public static String getAppSecret() {
        return LionUtil.getString("rabbitmq.sso.appsecret");
    }

    public static String getSSOUrl() {
        return LionUtil.getString("sso.base.url");
    }
}
