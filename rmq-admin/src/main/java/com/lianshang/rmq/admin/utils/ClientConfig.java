package com.lianshang.rmq.admin.utils;

import com.dianping.lion.EnvZooKeeperConfig;
import com.lianshang.common.utils.general.LionUtil;

/**
 * Created by Bao on 2016-03-02.
 */
public class ClientConfig {
    public static String getRunEnv() {

        return EnvZooKeeperConfig.getEnv();
    }

    public static String getClientVersion() {

        return  LionUtil.getString("boss.client.version");
    }

}
