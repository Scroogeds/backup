package com.leateck.gmp.backup.service;

import com.leateck.gmp.backup.mapper.BackupConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p>Title: BackupConfigService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Service
public class BackupConfigService implements IBackupConfigService {

    private String shellPath;

    @Value("${gmp-backup.shellPath}")
    public void setShellPath(String shellPath) {
        this.shellPath = shellPath.endsWith(File.separator) ? shellPath : shellPath + File.separator;
    }

    private BackupConfigMapper backupConfigMapper;

    @Autowired
    public void setBackupConfigMapper(BackupConfigMapper backupConfigMapper) {
        this.backupConfigMapper = backupConfigMapper;
    }


}
