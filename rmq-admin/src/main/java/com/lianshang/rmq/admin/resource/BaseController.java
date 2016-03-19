package com.lianshang.rmq.admin.resource;

import com.dianping.lion.EnvZooKeeperConfig;
import com.lianshang.common.utils.general.LionUtil;
import com.lianshang.sso.api.response.TokenResponse;
import com.lianshang.sso.api.utils.CookieUtil;
import com.lianshang.sso.api.utils.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bao on 2016-03-02.
 */

public class BaseController {
    public final String USERTOKEN = "usertoken";
    public final String USERTICKET = "ticket";

    private String runEnv = "";
    private boolean isProduct = false;
    private String baseWebUrl = "";
    public String appName = "RMQ-Admin";

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public String getRunEnv() {
        runEnv = EnvZooKeeperConfig.getEnv();
        return runEnv;
    }


    public boolean isProduct() {
        getRunEnv();
        if (runEnv == "product") {
            isProduct = true;
        }
        return isProduct;
    }

    private String ssoUrl = "";

    public String getSsoUrl() {
        String lionSsoBaseUrl = LionUtil.getString("sso.base.url");
        if (lionSsoBaseUrl != null && lionSsoBaseUrl.trim().length() > 0) {
            ssoUrl = lionSsoBaseUrl;
        }
        return ssoUrl;
    }

    public String getAppKey() {
        return LionUtil.getString("rabbitmq.sso.appKey");
    }

    public String getAppSecret() {
        return LionUtil.getString("rabbitmq.sso.appsecret");
    }


    public String getBaseWebUrl(HttpServletRequest request) {

        if (StringUtils.isEmpty(baseWebUrl)) {
            String path = request.getContextPath();
            baseWebUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
        }
        return baseWebUrl;
    }


    public String authUrl = getSsoUrl() + "/authorize";
    public String logoutUrl = getSsoUrl() + "/logout";

    public String getLoginUrl(HttpServletRequest req) throws Exception {

        String state = getBaseWebUrl(req) + "?v=" + System.currentTimeMillis();
        String callback = getBaseWebUrl(req) + "/callback";

        String loginUrl = authUrl + "?appkey=" + getAppKey() + "&state=" + java.net.URLEncoder.encode(state, "utf-8") + "&returnurl=" + java.net.URLEncoder.encode(callback, "utf-8");

        return loginUrl;
    }

    public String getLogoutUrl(HttpServletRequest req) throws Exception {

        String state = getBaseWebUrl(req) + "?v=" + System.currentTimeMillis();
        String callback = getBaseWebUrl(req) + "/callback";

        String loginUrl = logoutUrl + "?appkey=" + getAppKey() + "&state=" + java.net.URLEncoder.encode(state, "utf-8") + "&returnurl=" + java.net.URLEncoder.encode(callback, "utf-8");

        return loginUrl;
    }
}
