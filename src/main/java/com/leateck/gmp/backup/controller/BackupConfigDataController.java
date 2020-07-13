package com.leateck.gmp.backup.controller;

import com.leateck.gmp.backup.service.IBackupConfigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Title: BackupConfigDataController</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
@Controller
@RequestMapping("/backup/data")
public class BackupConfigDataController {

    private IBackupConfigDataService backupConfigDataService;

    @Autowired
    public void setBackupConfigDataService(IBackupConfigDataService backupConfigDataService) {
        this.backupConfigDataService = backupConfigDataService;
    }


}
