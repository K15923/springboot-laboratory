package com.k.quartz.task;

import com.k.quartz.service.BackupRecoveryService;
import com.k.quartz.service.impl.BackupRecoveryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

/**
 * @author k 2023/4/14 16:20
 */
@Slf4j
public class BackupJob implements Job {

    private final String SETTING_FILE_PATH = "D:\\backup\\setting.properties";
    private final String FILE_PATH = "D:\\backup";
    private Properties properties = new Properties();

    @Override
    public void execute(JobExecutionContext context) {
        //  备份逻辑
        try {
            File file = new File(FILE_PATH);
            File[] files = file.listFiles();
            long timestamp = 0L;
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    String name = file1.getName();
                    if (name.endsWith("自动备份")) {
                        long l = file1.lastModified();
                        if (l > timestamp) {
                            timestamp = l;
                        }
                    }
                }
            }
            LocalDateTime lastBackup = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            System.out.println(lastBackup);
            long daysBetween = ChronoUnit.DAYS.between(lastBackup.toLocalDate(), LocalDate.now()); // 计算相隔的天数
            System.out.println("Days between: " + daysBetween); // 输
            InputStream inputStream = new FileInputStream(SETTING_FILE_PATH);
            properties.load(inputStream);
            Object backupCycle = properties.get("backupCycle");
            Integer integer = Integer.valueOf(backupCycle.toString());
            if (integer >= daysBetween) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BackupRecoveryService backupRecoveryService = new BackupRecoveryServiceImpl();
        backupRecoveryService.backup("自动备份");
        System.out.println("Hello, Quartz!" + LocalDateTime.now());
    }
}
