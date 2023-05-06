package com.k.quartz.task;

import com.k.quartz.entity.Setting;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.support.CronSequenceGenerator;

/**
 * @author k 2023/4/14 16:20
 */
@Slf4j
public class QuartzManager {

    private Scheduler scheduler;

    public QuartzManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addJob(String jobName, String groupName, String triggerName, String cronExpression,
                       Class<?> clazz) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) clazz).withIdentity(jobName, groupName).build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName).withSchedule(
                CronScheduleBuilder.cronSchedule(cronExpression)).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void modifyJob(String groupName, String triggerName, String cronExpression) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
        CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (oldTrigger == null) {
            return;
        }
        String oldCronExpression = oldTrigger.getCronExpression();
        if (!oldCronExpression.equalsIgnoreCase(cronExpression)) {
            CronTrigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName).withSchedule(
                    CronScheduleBuilder.cronSchedule(cronExpression)).build();
            scheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }

    public void deleteJob(String jobName, String groupName, String triggerName) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
        JobKey jobKey = new JobKey(jobName, groupName);
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
    }

    public String getCron(Setting setting) {
        // 定义周期参数
        // int cycle = setting.getBackupCycle(); // 多少天执行一次，需要每天循环判断
        int hour = Integer.valueOf(setting.getBackupTime()); // 每天的 1 点执行任务
        // 使用 CronSequenceGenerator 生成 Cron 表达式
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("0 0 " + hour + " * * ?");
        log.info("cronSequenceGenerator ： {}", cronSequenceGenerator);
        return cronSequenceGenerator.toString();
    }
}

