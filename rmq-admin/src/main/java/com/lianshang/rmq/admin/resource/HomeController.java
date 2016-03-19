package com.lianshang.rmq.admin.resource;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.lianshang.common.utils.general.GeneralResult;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.sso.api.dto.SysButtonSimple;
import com.lianshang.sso.api.dto.UserPermissionTree;
import com.lianshang.sso.api.response.TokenResponse;
import com.lianshang.sso.api.utils.CookieUtil;
import com.lianshang.sso.api.utils.SSOPermissionUtils;
import com.lianshang.sso.api.utils.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bao on 2016-03-02.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private String indexUrl = "/";

    @RequestMapping(value = "/temp", method = RequestMethod.GET)
    public String index(ModelMap model) {

        model.addAttribute("data", "test!!!!");

        return "temp";
    }


    @RequestMapping(value = "/navigation", method = RequestMethod.GET)
    public String navigation(
            HttpServletRequest req, HttpServletResponse resp, ModelMap model
    ) throws Exception {


        String loginUrl = getLoginUrl(req);
        String token = com.lianshang.sso.api.utils.CookieUtil.getCookie(req, USERTOKEN);

        if (StringUtils.isEmpty(token)) {
            resp.sendRedirect(loginUrl);
            return null;
        } else {
            SSOPermissionUtils permissionUtils = new SSOPermissionUtils(getSsoUrl());
            List<UserPermissionTree> authMenuList = permissionUtils.getAuthMenu(getAppKey(), getAppSecret(), token);

            Map<String, Object> map = ImmutableMap.<String, Object>builder()
                    .put("authMenuList", authMenuList)
                    .put("appName", appName)
                    .build();

            // ModelAndView view = new ModelAndView("temp.ftl");
            //view.addObject("data", ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
            //return view;
            //model.addAttribute("data", ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
            model.addAttribute("data",ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
            return "navigation";

        }


    }


    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public String callback(
            HttpServletRequest req, HttpServletResponse resp,
            String ticket,
            String state, ModelMap model) throws Exception {


        String token = com.lianshang.sso.api.utils.CookieUtil.getCookie(req, USERTOKEN);
        String ticketCookie = com.lianshang.sso.api.utils.CookieUtil.getCookie(req, USERTICKET);
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
            if (!StringUtils.isEmpty(state)) {
                resp.sendRedirect(state);
            } else {
                resp.sendRedirect(indexUrl);
            }
            return null;
        } catch (Exception ex) {
            LOGGER.error("获取用户信息出现异常({})", new Object[]{ex});
//            ModelAndView view = new ModelAndView("/home/callback.ftl");
//            view.addObject("data", ResponseUtil.wrap(ex, "获取用户信息出现异常!"));
//            return view;
            if (ex.getMessage().indexOf("重新登录") >= 0) {
                String loginUrl = getLoginUrl(req);
                CookieUtil.deleteCookie(resp, USERTOKEN);
                CookieUtil.deleteCookie(resp, USERTICKET);
                resp.sendRedirect(loginUrl);
                return null;
            }
            model.addAttribute(ResponseUtil.wrap(ex, "获取用户信息出现异常!"));
            return "callback";
        }
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
            HttpServletRequest req,
            HttpServletResponse resp,
            ModelMap model
    ) throws Exception {

        String loginUrl = getLoginUrl(req);
        try {
            String token = CookieUtil.getCookie(req, USERTOKEN);
            String ticket = CookieUtil.getCookie(req, USERTICKET);
            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(ticket)) {
                resp.sendRedirect(loginUrl);
                return null;
            } else {

                String appSecret = getAppSecret();
                SSOTokenUtils tokenUtils = new SSOTokenUtils(getSsoUrl());
                TokenResponse tokenResponse = tokenUtils.getUserInfo(getAppKey(), appSecret, ticket);

                SSOPermissionUtils permissionUtils = new SSOPermissionUtils(getSsoUrl());
                List<UserPermissionTree> authMenuList = permissionUtils.getAuthMenu(getAppKey(), getAppSecret(), token);
                if(authMenuList==null || authMenuList.isEmpty()){
                    model.addAttribute(ResponseUtil.wrap(-3,"您没有系统【"+super.appName+"】的访问权限,请联系系统管理员!"));
                    return "callback";
                }

                Map<Integer, List<SysButtonSimple>> buttonList = new HashMap<Integer, List<SysButtonSimple>>();
                for (UserPermissionTree menu : authMenuList) {
                    for (UserPermissionTree subMenu : menu.getSubMenuList()) {
                        buttonList.put(subMenu.getSysTableId(), subMenu.getButtonList());
                    }
                }



                Map<String, Object> map = ImmutableMap.<String, Object>builder()
                        .put("user", tokenResponse)
                                //.put("authMenuList", JSON.toJSONString(authMenuList))
                        .put("authButtonList", JSON.toJSONString(buttonList))
                        .put("loginUrl", loginUrl)
                        .put("appName", appName)
                        .build();

//                ModelAndView view = new ModelAndView("/home/index.ftl");
//                view.addObject("data", ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
//                return view;
                model.addAttribute("data",ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "成功", map));
                return "index";
            }
        } catch (Exception e) {
            if (e.getMessage().indexOf("重新登录") >= 0) {

                CookieUtil.deleteCookie(resp, USERTOKEN);
                CookieUtil.deleteCookie(resp, USERTICKET);
                resp.sendRedirect(loginUrl);
                return null;
            }

//            ModelAndView view = new ModelAndView("/home/callback.ftl");
//            view.addObject("data", ResponseUtil.wrap(e, "获取用户信息出现异常,请刷新后重试!"));
//            return view;
            model.addAttribute(ResponseUtil.wrap(e, "获取用户信息出现异常,请刷新后重试!"));
            return "callback";
        }


    }



    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(
            HttpServletRequest req,
            HttpServletResponse resp,
            ModelMap model
    ) throws Exception {


        String tokenCookie = com.lianshang.sso.api.utils.CookieUtil.getCookie(req, USERTOKEN);
        String ticketCookie = com.lianshang.sso.api.utils.CookieUtil.getCookie(req, USERTICKET);
        if ( StringUtils.isEmpty(ticketCookie) || StringUtils.isEmpty(tokenCookie)) {
            resp.sendRedirect(indexUrl);
            return null;
        }


        String logoutUrl = getLogoutUrl(req);
        CookieUtil.deleteCookie(resp, USERTOKEN);
        CookieUtil.deleteCookie(resp, USERTICKET);
        resp.sendRedirect(logoutUrl);
        return null;
    }



}