package com.k.quartz.controller;

import com.k.quartz.service.LoginService;
import com.k.quartz.entity.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户登录")
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @GetMapping("login")
    public Result login(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
                        HttpServletResponse response) {
        return loginService.login(username, password, request, response);
    }

    @GetMapping("update-login")
    public Result updateLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
                        HttpServletResponse response) {
        return loginService.updateLogin(username, password, request, response);
    }

}
