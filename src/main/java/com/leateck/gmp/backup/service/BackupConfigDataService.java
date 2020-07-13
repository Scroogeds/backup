package com.leateck.gmp.backup.service;

import com.leateck.gmp.backup.mapper.BackupConfigDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Title: BackupConfigDataService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
@Service
public class BackupConfigDataService implements IBackupConfigDataService {

    private BackupConfigDataMapper backupConfigDataMapper;

    @Autowired
    public void setBackupConfigDataMapper(BackupConfigDataMapper backupConfigDataMapper) {
        this.backupConfigDataMapper = backupConfigDataMapper;
    }


}
