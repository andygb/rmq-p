package com.lianshang.rmq.admin.utils;

import com.lianshang.sso.api.Exception.SecurityControlException;
import com.lianshang.sso.api.security.SecurityControl;
import com.lianshang.sso.api.utils.SSOPermissionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yuan.zhong on 2016-03-21.
 *
 * @author yuan.zhong
 */
public class SecurityCheckUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityCheckUtil.class);

    public static boolean checkPermission(SecurityControl securityControl, String token) {
        return checkPermission(securityControl.sysTableId(),securityControl.btnScript(),securityControl.isButton(),token);
    }

    public static boolean checkPermission(String sysTableId,String btnScript,boolean isButton,String token) {
        try {
            if (StringUtils.isEmpty(sysTableId)) {
                throw new SecurityControlException("参数错误,sysTableId不能为空");
            }

            SSOPermissionUtils permissionUtils = new SSOPermissionUtils(SSOConfig.getSSOUrl());
            if (isButton) {
                if (StringUtils.isEmpty(btnScript)) {
                    throw new SecurityControlException("参数错误,按钮名称不能为空!");
                }


                return permissionUtils.checkPermission(SSOConfig.getAppKey(), SSOConfig.getAppSecret(), token, sysTableId, btnScript);
            } else {
                return permissionUtils.checkPermission(SSOConfig.getAppKey(), SSOConfig.getAppSecret(), token, sysTableId, "");
            }
        } catch (Exception e) {
            LOGGER.error("检测权限出现异常", e);
            return false;
        }
    }
}
