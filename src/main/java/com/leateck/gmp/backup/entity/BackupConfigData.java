package com.leateck.gmp.backup.entity;

import lombok.Data;

/**
 * <p>Title: BackupConfigData</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
@Data
public class BackupConfigData {

    private String id;

    private String backupConfigId;

    private String shellCommands;

    private String backupPaths;

    private String cronExpr;

    private String filename;

}
