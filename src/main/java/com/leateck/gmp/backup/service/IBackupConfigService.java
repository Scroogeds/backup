package com.leateck.gmp.backup.service;

import com.leateck.gmp.backup.entity.BackupConfig;
import com.leateck.gmp.backup.vo.BackupConfigVo;

import java.util.List;

/**
 * <p>Title: IBackupConfigService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
public interface IBackupConfigService {

    List<BackupConfig> queryBackupConfig();

    BackupConfig queryBackupConfigById(String id);

    List<BackupConfig> addBackupConfig(BackupConfig backupConfig);

    int modifyBackupConfig(BackupConfig backupConfig);

    int deleteBackupConfig(String id);

    void initH2DataBase();

    void saveH2DataBase();

    String executeShellFile(String id);
}
