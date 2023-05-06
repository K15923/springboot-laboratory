package com.k.quartz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@ApiModel(description = "备份文件信息")
public class BackupFileInfo {
    @ApiModelProperty(value = "文件路径", example = "/data/backup")
    private String filePath;

    @ApiModelProperty(value = "文件名称", example = "backup-20220413.tar.gz")
    private String fileName;

    @ApiModelProperty(value = "备份时间", example = "2022-04-13 10:30:00")
    private Date backupTime;

    @ApiModelProperty(value = "备份文件大小，单位为字节", example = "1024")
    private long fileSize;

    @ApiModelProperty(value = "备份信息", example = "备份成功")
    private String backupInfo;


}
