package com.k.quartz.service;

import com.k.quartz.entity.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    Result login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    Result updateLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
