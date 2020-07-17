package com.leateck.gmp.backup.core.entity;

import com.leateck.gmp.backup.constant.BackupConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>Title: BackupConfig</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@ApiModel(value = "BackupConfig", description = "备份配置表")
public class BackupConfig extends BaseData{

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String code;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describe;

    /**
     * 定时任务表达式
     */
    @ApiModelProperty(value = "定时任务表达式")
    private String cronExpr;

    /**
     * cron支持的方式 0-linux 1-java
     */
    @ApiModelProperty(value = "cron支持的方式 0-linux 1-java")
    private String cronType = BackupConstant.DEFAULT_CORN_EXPR_TYPE;

}
