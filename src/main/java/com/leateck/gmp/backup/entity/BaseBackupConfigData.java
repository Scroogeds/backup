package com.leateck.gmp.backup.entity;

import lombok.Data;

/**
 * <p>Title: BaseBackupConfigData</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-10   luyangqian  Created
 * </pre>
 */
@Data
public class BaseBackupConfigData {

    private String id;

    private String backupConfigId;

    private String shellCommands;

    private String cronExpr;

    private String filename;

    private String backupFilename = "gmp-backup";

    private int saveDayNum = 0;

}
