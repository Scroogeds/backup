package com.leateck.gmp.backup.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Title: DatabaseProperties</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Data
@Component
@ConfigurationProperties(prefix = "gmp-backup.database")
public class DatabaseProperties {

    private String enabled;

    private String path;

}
