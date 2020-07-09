package com.leateck.gmp.backup.boot;

import com.leateck.gmp.backup.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * <p>Title: BackupBootApplicationRunner</p>
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
@Order(LOWEST_PRECEDENCE - 10)
public class BackupBootApplicationRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(BackupBootApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, IBackupApplicationRunner> beansOfType = SpringContextUtil
                .getApplicationContext().getBeansOfType(IBackupApplicationRunner.class);
        for (Map.Entry<String, IBackupApplicationRunner> runner : beansOfType.entrySet()) {
            logger.info("server [" + runner.getValue().getClass() + "] --->>>boot start deal");
            runner.getValue().run(args);
            logger.info("server [" + runner.getValue().getClass() + "] --->>>boot end deal");
        }
    }
}
