package com.k.quartz.service.impl;

import com.k.quartz.entity.Result;
import com.k.quartz.entity.Setting;
import com.k.quartz.service.SettingService;
import com.k.quartz.task.BackupJob;
import com.k.quartz.task.QuartzManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.Properties;

@Slf4j
@Service
public class SettingServiceImpl implements SettingService {

    @Value("${backup.settingFilePath}")
    private String SETTING_FILE_PATH;

    private Properties properties = new Properties();

    @Resource
    private QuartzManager quartzManager;

    @Override
    public Result setting(Setting setting) {
        log.info("修改设置信息如下：{}", setting);
        try {
            OutputStream outputStream = new FileOutputStream(SETTING_FILE_PATH);
            properties.setProperty("backupCycle", String.valueOf(setting.getBackupCycle()));
            properties.setProperty("backupTime", String.valueOf(setting.getBackupTime()));
            properties.setProperty("backupType", setting.getBackupType());
            properties.store(outputStream, "");

            // 保存完属性文件，直接生成定时任务，生成定时任务之前，先删掉之前的定时任务
            String jobName = "backup_job";
            String groupName = "backup_group";
            String triggerName = "backup_trigger";
            quartzManager.deleteJob(jobName, groupName, triggerName);
            if ("自动备份".equals(setting.getBackupType())) {
                String cronExpression = quartzManager.getCron(setting);
                // todo 测试环境用
                log.info("cronExpression: 为 {}", cronExpression);
                cronExpression = "*/5 * * * * ?";
                quartzManager.addJob(jobName, groupName, triggerName, cronExpression, BackupJob.class);
            }
            return Result.ok("保存成功", properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SchedulerException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Result querySetting() {
        try {
            InputStream inputStream = new FileInputStream(SETTING_FILE_PATH);
            properties.load(inputStream);
            return Result.ok(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
