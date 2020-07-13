package com.leateck.gmp.backup.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.leateck.gmp.backup.service.IBackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PutMapping("/build/{backupConfigDataId}")
    public String buildShell(@PathVariable("backupConfigDataId") String backupConfigDataId) {
        return backupConfigService.buildShellFile(backupConfigDataId);
    }

    @ResponseBody
    @PostMapping("/execute/{backupConfigDataId}")
    public String executeShell(@PathVariable("backupConfigDataId") String backupConfigDataId) {
        return backupConfigService.executeShell(backupConfigDataId);
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

    @ResponseBody
    @GetMapping("/command/install-sshpass")
    public String installSSHPass(){
        String executeSSHPass = "yum -y install sshpass";
        /*return RuntimeUtil.execForStr(executeSSHPass);*/
        return executeSSHPass;
    }
}
