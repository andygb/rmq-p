package com.lianshang.rmq.admin.resource;

import com.dianping.lion.EnvZooKeeperConfig;
import com.lianshang.common.utils.general.LionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bao on 2016-03-02.
 */

public class BaseResource {
    public final String USERTOKEN="usertoken";
    public final String USERTICKET="ticket";
    private String runEnv="";
    private boolean isProduct=false;
    private  String baseWebUrl = "";
    public String appName="rmq-admin";


    public String getRunEnv() {
        runEnv= EnvZooKeeperConfig.getEnv();
        return runEnv;
    }


    public boolean isProduct() {
        getRunEnv();
        if(runEnv=="product"){
            isProduct=true;
        }
        return isProduct;
    }

    private String ssoUrl="http://localhost:8088/sso";

    public String getSsoUrl() {
        String lionSsoBaseUrl = LionUtil.getString("sso.base.url");
        if (lionSsoBaseUrl != null && lionSsoBaseUrl.trim().length() > 0) {
            ssoUrl = lionSsoBaseUrl;
        }
        return ssoUrl;
    }

    public String getAppKey(){
        return LionUtil.getString("boss.sso.appkey");
    }

    public String getAppSecret(){
        return LionUtil.getString("boss.sso.appsecret");
    }


    public String getBaseWebUrl(HttpServletRequest request) {

        if(StringUtils.isEmpty(baseWebUrl)){
            String path = request.getContextPath();
            baseWebUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
        }
        return baseWebUrl;
    }


}
