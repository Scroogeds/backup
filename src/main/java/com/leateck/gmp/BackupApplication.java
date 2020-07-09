package com.leateck.gmp;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Title: BackupApplication</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-06-28   luyangqian  Created
 * </pre>
 */
@MapperScan("com.leateck.gmp.backup.mapper")
@SpringBootApplication
public class BackupApplication {

    private static Logger logger = LoggerFactory.getLogger(BackupApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(BackupApplication.class, args);

        logger.info("----------踏遍青山人未老，风景这边独好----------");
    }

}
