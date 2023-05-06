package com.k.quartz.service.impl;

import com.k.quartz.entity.Const;
import com.k.quartz.entity.Result;
import com.k.quartz.entity.vo.LoginVO;
import com.k.quartz.service.LoginService;
import com.k.quartz.utils.RequestUtils;
import com.k.quartz.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;
import java.util.Properties;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${backup.userInfoFilePath}")
    private String userInfoFilePath;

    private Properties properties = new Properties();

    @Override
    public Result<LoginVO> login(String username, String password, HttpServletRequest request,
                                 HttpServletResponse response) {
        log.info("用户登录开始：用户名：{},密码：{}", username, password);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(userInfoFilePath);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LoginVO loginVo = new LoginVO();
        if (properties.get("username").toString().equals(username)) {
            if (decode(properties.get("password").toString()).equals(password)) {
                loginVo.setAdminUser(username);
                loginVo.setToken(TokenUtils.createToken(username));
                RequestUtils.setCookie(response, Const.COOKIE_NAME, loginVo.getToken());
                request.setAttribute(Const.TOKEN, loginVo.getToken());
                return Result.ok("登录成功", loginVo);
            } else {
                return Result.error("用户名或密码错误");
            }
        } else {
            return Result.error("登录失败，请重试");
        }
    }

    @Override
    public Result updateLogin(String username, String password, HttpServletRequest request,
                              HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(userInfoFilePath);
            properties.setProperty("username", username);
            properties.setProperty("password", encoder(password.getBytes()));
            properties.store(outputStream, "修改用户名密码！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok("修改用户名密码成功！");
    }


    private String encoder(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }


}
