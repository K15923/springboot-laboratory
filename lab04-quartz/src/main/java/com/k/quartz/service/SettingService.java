package com.k.quartz.service;

import com.k.quartz.entity.Result;
import com.k.quartz.entity.Setting;

public interface SettingService {

    Result setting(Setting setting);

    Result querySetting();
}
