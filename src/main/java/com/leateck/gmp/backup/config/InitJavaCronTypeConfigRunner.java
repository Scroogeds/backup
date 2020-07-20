package com.leateck.gmp.backup.config;

import com.leateck.gmp.backup.boot.IBackupApplicationRunner;
import com.leateck.gmp.backup.core.service.IBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * <p>Title: InitJavaCronTypeConfigRunner</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-20   luyangqian  Created
 * </pre>
 */
@Component
@Order(LOWEST_PRECEDENCE - 4)
public class InitJavaCronTypeConfigRunner implements IBackupApplicationRunner {

    //private static Logger logger = LoggerFactory.getLogger(InitJavaCronTypeConfigRunner.class);

    @Autowired
    private IBackupService backupService;

    @Override
    public void run(ApplicationArguments args) {
        backupService.initJavaCron();
    }
}
