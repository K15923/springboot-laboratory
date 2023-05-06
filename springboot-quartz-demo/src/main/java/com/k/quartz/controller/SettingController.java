package com.k.quartz.controller;

import com.k.quartz.service.SettingService;
import com.k.quartz.entity.Result;
import com.k.quartz.entity.Setting;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "设置定时备份")
@RestController
public class SettingController {

    @Resource
    private SettingService settingService;

    @PostMapping("setting")
    @ApiOperation(value = "保存定时备份设置", notes = "保存定时备份设置的接口")
    public Result setting(@RequestBody Setting setting) {
        return settingService.setting(setting);
    }

    @GetMapping("query-setting")
    @ApiOperation(value = "查询定时备份设置", notes = "查询定时备份设置的接口")
    public Result querySetting() {
        return settingService.querySetting();
    }
}
