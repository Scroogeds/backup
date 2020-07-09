package com.leateck.gmp.backup.controller;

import com.leateck.gmp.backup.entity.BackupConfig;
import com.leateck.gmp.backup.service.IBackupConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping
    public String queryBackupConfigs(Model model) {
        model.addAttribute("backupConfigs", backupConfigService.queryBackupConfig());
        return "backupConfig";
    }

    @GetMapping("/{id}")
    public String queryBackupConfigById(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("backupConfig", backupConfigService.queryBackupConfigById(id));
        return "backupConfigUpdate";
    }

    @ResponseBody
    @PutMapping("/{id}")
    public String modifyBackupConfig(@PathVariable("id") String id, Model model,
                               @RequestBody BackupConfig backupConfig) {
        backupConfig.setId(id);
        /*model.addAttribute("backupConfigs", backupConfigService.modifyBackupConfig(backupConfig));
        return "backupConfig";*/
        backupConfigService.modifyBackupConfig(backupConfig);
        return "success";
    }

    @PostMapping
    public String addBackupConfig(Model model, HttpServletRequest request) {
        String sysType = request.getParameter("sysType");
        String address = request.getParameter("address");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String connectType = request.getParameter("connectType");
        BackupConfig backupConfig = new BackupConfig();
        backupConfig.setSysType(sysType);
        backupConfig.setAddress(address);
        backupConfig.setUsername(username);
        backupConfig.setPassword(password);
        backupConfig.setConnectType(connectType);
        model.addAttribute("backupConfigs", backupConfigService.addBackupConfig(backupConfig));
        return "backupConfig";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public String deleteBackup(@PathVariable("id") String id, Model model) {
        if (backupConfigService.deleteBackupConfig(id) == 0) {
            return "error";
        }
        return "success";
    }
}
