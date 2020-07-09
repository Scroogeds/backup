package com.leateck.gmp.backup.config;

import com.leateck.gmp.backup.boot.IBackupApplicationRunner;
import com.leateck.gmp.backup.service.BackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * <p>Title: InitDataBaseRunner</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
@Component
@Order(LOWEST_PRECEDENCE - 5)
public class InitDatabaseRunner implements IBackupApplicationRunner {

    @Autowired
    private BackupConfigService backupConfigService;

    @Override
    public void run(ApplicationArguments args) {
        //backupConfigService.initH2DataBase();
    }
}
