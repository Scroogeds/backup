package com.leateck.gmp.backup.entity;

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
public class BackupConfig extends BaseData {

    private String code;

    private String name;

    private String cronExpr;

}
