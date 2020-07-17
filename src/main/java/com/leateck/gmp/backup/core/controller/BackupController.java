package com.leateck.gmp.backup.core.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.core.service.IBackupService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/backup")
public class BackupController {

    private IBackupService backupService;

    @Autowired
    public void setBackupService(IBackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/build/{code}")
    public Result<String> buildShell(@PathVariable("code") String code) {
        return backupService.buildShell(code);
    }

    @PostMapping("/execute/{code}")
    public Result<String> executeShell(@PathVariable("code") String code) {
        return backupService.executeShell(code);
    }

    @PostMapping("/cron/{code}")
    public Result<String> buildCron(@PathVariable("code") String code) {
        return backupService.buildCron(code);
    }

    @DeleteMapping("/cron/{code}")
    public Result<String> removeCron(@PathVariable("code") String code) {
        return backupService.removeCron(code);
    }

    /**
     * 执行命令
     * @param cmd
     * @return
     */
    @GetMapping("/command")
    public String executeCommand(@RequestParam("cmd") String cmd){
        return RuntimeUtil.execForStr(cmd);
    }
}
