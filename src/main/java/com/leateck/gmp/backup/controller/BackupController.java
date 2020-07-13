package com.leateck.gmp.backup.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.leateck.gmp.backup.service.IBackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>Title: BackupController</p>
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
@RequestMapping("/backup")
public class BackupController {

    private IBackupConfigService backupConfigService;

    @Autowired
    public void setBackupConfigService(IBackupConfigService backupConfigService) {
        this.backupConfigService = backupConfigService;
    }

    /**
     * 执行命令
     * @param cmd
     * @return
     */
    @ResponseBody
    @GetMapping("/command")
    public String executeCommand(@RequestParam("cmd") String cmd){
        return RuntimeUtil.execForStr(cmd);
    }
}
