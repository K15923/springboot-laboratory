package com.k.quartz.utils;

import com.alibaba.fastjson2.JSON;
import com.k.quartz.entity.vo.LoginVO;
import com.k.quartz.entity.Const;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Optional;


/**
 * @author k 2023/4/18 9:15
 */
public class TokenUtils {
    public static String createToken(String adminName) {
        LoginVO loginVO = new LoginVO();
        loginVO.setAdminUser(adminName);
        loginVO.setTimestamp(System.currentTimeMillis());
        return Base64.getEncoder().encodeToString(JSON.toJSONString(loginVO).getBytes());
    }

    public static String getAdminName(HttpServletRequest request) {
        try {
            String token = Optional.ofNullable(request.getHeader(Const.TOKEN)).orElse(
                    (String) request.getAttribute(Const.TOKEN));
            if (token == null) {
                return null;
            }
            LoginVO loginVO = JSON.parseObject(new String(Base64.getDecoder().decode(token)), LoginVO.class);
            if (loginVO == null) {
                return null;
            }
            return loginVO.getAdminUser();
        } catch (Exception e) {
            return null;
        }
    }
}
