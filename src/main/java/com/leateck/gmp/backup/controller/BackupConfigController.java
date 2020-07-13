package com.leateck.gmp.backup.controller;

import com.leateck.gmp.backup.service.IBackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Title: BackupController</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-06-28   luyangqian  Created
 * </pre>
 */
@Controller
@RequestMapping("/backup/config")
public class BackupConfigController {

    /*private Map<String, BackupConfigData> connectInformationMap = new ConcurrentHashMap<>();*/

    private IBackupConfigService backupConfigService;

    @Autowired
    public void setBackupConfigService(IBackupConfigService backupConfigService) {
        this.backupConfigService = backupConfigService;
    }


}
