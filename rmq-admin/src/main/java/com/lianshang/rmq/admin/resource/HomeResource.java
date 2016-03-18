package com.lianshang.rmq.admin.resource;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.lianshang.common.utils.general.GeneralResult;
import com.lianshang.common.utils.general.LionUtil;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.sso.api.dto.UserPermissionTree;
import com.lianshang.sso.api.response.TokenResponse;
import com.lianshang.sso.api.utils.CookieUtil;
import com.lianshang.sso.api.utils.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Bao on 2016-03-02.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HomeResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeResource.class);

    private String authUrl = getSsoUrl() + "/authorize";

    private String indexUrl = "/index";


    @GET
    @Path("/callback")
    @Produces(MediaType.TEXT_HTML)
    public Viewable callback(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @QueryParam("ticket") String ticket,
            @QueryParam("state") String state) throws Exception {


        String token = CookieUtil.getCookie(req, USERTOKEN);
        String ticketCookie = CookieUtil.getCookie(req, USERTICKET);
        if ((!StringUtils.isEmpty(token) && !StringUtils.isEmpty(ticketCookie)) || StringUtils.isEmpty(ticket)) {
            //没有token 或则 非正常访问
            resp.sendRedirect(indexUrl);
            return null;
        }

        GeneralResult<TokenResponse> responseGeneralResult = null;
        try {
            String appSecret = getAppSecret();
            SSOTokenUtils tokenUtils = new SSOTokenUtils(getSsoUrl());
            TokenResponse tokenResponse = tokenUtils.getUserInfo(getAppKey(), appSecret, ticket);

            CookieUtil.addCookie(resp, USERTOKEN, tokenResponse.getToken(), 3600);
            CookieUtil.addCookie(resp, USERTICKET, ticket, 3600);
            CookieUtil.addCookie(resp, "usertoken", "tO-94T140sMcihcF0LD_WSM3iE_M7KQ6", 3600);
            if (!StringUtils.isEmpty(state)) {
                resp.sendRedirect(state);
            } else {
                resp.sendRedirect(indexUrl);
            }
            return null;
        } catch (Exception ex) {
            LOGGER.error("获取用户信息出现异常({})", new Object[]{ex});
            return new Viewable("/home/callback.ftl", ResponseUtil.wrap(ex, "获取用户信息出现异常!"));
        }
    }


    @GET
    @Path("/")
    @Produces("text/html;charset=UTF-8")
    public Viewable test(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp
    ) throws IOException{


        String loginUrl = LionUtil.getString("boss.phpWebUrl");
        try {
            String token = CookieUtil.getCookie(req, USERTOKEN);

            TokenResponse tokenResponse = new TokenResponse();

            List<UserPermissionTree> authMenuList =new ArrayList<UserPermissionTree>();

            Map<String, Object> map = ImmutableMap.<String, Object>builder()
                    .put("user", tokenResponse)
                    .put("authMenuList", JSON.toJSONString(authMenuList))
                    .put("loginUrl", loginUrl)
                    .put("appName", appName)
                    .build();

            return new Viewable("/home/index.ftl", ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
        } catch (Exception e) {
            if(e.getMessage().indexOf("重新登录")>=0){

                CookieUtil.deleteCookie(resp, USERTOKEN);
                resp.sendRedirect(loginUrl);
                return null;
            }

            return new Viewable("/home/callback.ftl", ResponseUtil.wrap(e, "获取用户信息出现异常,请刷新后重试!"));
        }


    }


}