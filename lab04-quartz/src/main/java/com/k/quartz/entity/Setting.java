package com.k.quartz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "备份设置")
public class Setting {
    @ApiModelProperty(value = "IP地址", example = "192.168.0.1", hidden = true)
    private String ipAddress;

    @ApiModelProperty(value = "端口号", example = "8080", hidden = true)
    private int port;

    @ApiModelProperty(value = "自动备份路径", example = "./backup", hidden = true)
    private String backupPath;

    @ApiModelProperty(value = "自动备份周期", example = "7", allowableValues = "3,7,15,30,120")
    private int backupCycle;

    @ApiModelProperty(value = "自动备份时间", example = "1")
    private int backupTime;

    @ApiModelProperty(value = "备份类型", example = "自动备份", allowableValues = "自动备份,手动备份")
    private String backupType;

    @ApiModelProperty(value = "备份时间", example = "2023-04-13 01:00:00", hidden = true)
    private String backupStartTime;

    @ApiModelProperty(value = "备份结果", example = "备份成功", allowableValues = "备份成功,备份失败", hidden = true)
    private String backupResult;

    @ApiModelProperty(value = "备份路径", example = "C:/Backup/20230413.zip", hidden = true)
    private String backupFilePath;


}
