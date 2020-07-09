package com.leateck.gmp.backup.service;

import com.leateck.gmp.backup.entity.BackupConfigData;
import com.leateck.gmp.backup.vo.BackupConfigDataVo;

import java.util.List;

/**
 * <p>Title: IBackupConfigDataService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
public interface IBackupConfigDataService {

    List<BackupConfigData> queryByBackupConfigId(String backupConfigId);

    List<BackupConfigData> addBackupConfigData(BackupConfigData backupConfigData);

    int modifyById(BackupConfigDataVo backupConfigDataVo);

    BackupConfigDataVo queryById(String id);

    int deleteById(String id);

}
