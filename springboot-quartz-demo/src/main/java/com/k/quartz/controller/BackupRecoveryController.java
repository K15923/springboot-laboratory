package com.k.quartz.controller;

import com.k.quartz.entity.Result;
import com.k.quartz.service.BackupRecoveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "手动备份恢复")
@RestController
public class BackupRecoveryController {

    @Resource
    private BackupRecoveryService backupRecoveryService;

    @GetMapping("backup")
    @ApiOperation(value = "手动备份", notes = "手动备份接口")
    public Result backup() {
        return backupRecoveryService.backup("手动备份");
    }

    @GetMapping("recovery")
    @ApiOperation(value = "手动恢复", notes = "手动备份接口")
    public Result recovery(@RequestParam String backupFile) {
        return backupRecoveryService.recovery(backupFile);
    }

    @GetMapping("query-status")
    @ApiOperation(value = "查询备份/恢复状态", notes = "查询备份/恢复状态")
    public Result queryStatus() {
        return backupRecoveryService.queryStatus();
    }


    @GetMapping("query-backup-list")
    @ApiOperation(value = "查询备份列表", notes = "查询备份列表接口")
    public Result queryBackupList() {
        return backupRecoveryService.queryBackupList();
    }
}
