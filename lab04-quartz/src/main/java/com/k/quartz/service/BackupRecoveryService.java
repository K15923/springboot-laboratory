package com.k.quartz.service;

import com.k.quartz.entity.Result;

public interface BackupRecoveryService {
    Result backup(String backupType);

    Result recovery(String backupFile);

    Result queryStatus();

    Result queryBackupList();
}
