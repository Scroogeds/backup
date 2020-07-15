package com.leateck.gmp.backup.config;

import com.leateck.gmp.backup.core.service.BackupConfigService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Title: SaveDatabaseDisposableBean</p>
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
public class SaveDatabaseDisposableBean implements DisposableBean {

    @Autowired
    private BackupConfigService backupConfigService;

    @Override
    public void destroy() throws Exception {

    }

}
