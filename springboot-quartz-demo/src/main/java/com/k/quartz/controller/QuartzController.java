package com.k.quartz.controller;

import com.k.quartz.entity.Result;
import com.k.quartz.entity.Setting;
import com.k.quartz.task.BackupJob;
import com.k.quartz.task.QuartzManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author k 2023/4/14 16:46
 */
@Slf4j
@Api(tags = "定时任务设置")
@RestController
public class QuartzController {

    @Resource
    private QuartzManager quartzManager;


    @GetMapping("add-job")
    @ApiOperation(value = "添加定时任务", notes = "添加定时任务")
    public Result addJob(String jobName, String groupName, String triggerName, Setting setting) {
        try {
            String cronExpression = quartzManager.getCron(setting);
            log.info("cronExpression: 为 {}", cronExpression);
            cronExpression = "*/5 * * * * ?";
            quartzManager.addJob(jobName, groupName, triggerName, cronExpression, BackupJob.class);
            return Result.ok("添加定时任务成功。。。");
        } catch (SchedulerException e) {
            log.error("Quartz添加定时任务成功");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("modify-job")
    @ApiOperation(value = "修改定时任务", notes = "修改定时任务接口")
    public Result modifyJob(String jobName, String groupName, String triggerName, Setting setting) {
        String cronExpression = quartzManager.getCron(setting);
        log.info("cronExpression: 为 {}", cronExpression);
        cronExpression = "*/10 * * * * ?";
        try {
            quartzManager.modifyJob(groupName, triggerName, cronExpression);
            return Result.ok("修改定时任务成功。。。");
        } catch (Exception e) {
            log.error("Quartz修改任务失败");
            throw new RuntimeException(e);
        }
    }


    @GetMapping("delete-job")
    @ApiOperation(value = "删除定时任务", notes = "删除定时任务接口")
    public Result deleteJob(String jobName, String groupName, String triggerName) {
        try {
            quartzManager.deleteJob(jobName, groupName, triggerName);
            return Result.ok("删除定时任务。。。");
        } catch (SchedulerException e) {
            log.error("Quartz删除定时任务");
            throw new RuntimeException(e);
        }
    }



}
