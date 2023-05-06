package com.k.quartz.config;

import com.k.quartz.task.QuartzManager;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author k 2023/4/14 16:19
 */
@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

    @Bean
    public QuartzManager quartzManager(Scheduler scheduler) {
        return new QuartzManager(scheduler);
    }
}
