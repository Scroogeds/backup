package com.leateck.gmp.backup.config;

import com.leateck.gmp.backup.handler.AutoValuationMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Title: MybatisConfiguration</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public AutoValuationMetaObjectHandler autoValuationMetaObjectHandler() {
        return new AutoValuationMetaObjectHandler();
    }
}
