package com.leateck.gmp.backup.core.entity;

import lombok.Data;

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
public class BackupConfig extends BaseData{

    /**
     * 编号
     */
    private String code;

    /**
     * 描述
     */
    private String describe;

    /**
     * 定时任务表达式
     */
    private String cronExpr;

    /**
     * cron支持的方式 0-linux 1-java
     */
    private String cronType;

}
