package com.lianshang.rmq.admin.security;

import com.lianshang.rmq.admin.utils.SecurityCheckUtil;
import com.lianshang.sso.api.security.SecurityControl;
import com.lianshang.sso.api.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuan.zhong on 2016-03-21.
 *
 * @author yuan.zhong
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            SecurityControl securityControl = ((HandlerMethod) handler).getMethodAnnotation(SecurityControl.class);

            if (securityControl != null)


                try {
                    String token= CookieUtil.getCookie(request, "usertoken");
                    if (!SecurityCheckUtil.checkPermission(securityControl, token)) {
                        LOGGER.error("SecurityControlInterceptor验证失败!");
                        result = false;
                    } else {
                        result = true;
                    }
                } catch (Exception e) {
                    LOGGER.error("SecurityControlInterceptor验证异常!", e);
                    result=false;
                    //throw new SecurityControlException("认证出现异常,请稍后重试.");
                }
        }
        if(!result){
            response.setStatus(598);
        }
        return result;

    }
}
