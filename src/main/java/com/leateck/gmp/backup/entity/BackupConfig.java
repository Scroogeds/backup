package com.leateck.gmp.backup.entity;

import lombok.Builder;
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
@Builder(toBuilder = true)
public class BackupConfig extends BaseData {

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

}
