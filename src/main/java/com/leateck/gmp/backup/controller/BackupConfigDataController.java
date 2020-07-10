package com.leateck.gmp.backup.controller;

import com.leateck.gmp.backup.entity.BackupConfigData;
import com.leateck.gmp.backup.service.IBackupConfigDataService;
import com.leateck.gmp.backup.vo.BackupConfigDataVo;
import com.leateck.gmp.backup.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/{backupConfigId}")
    public String queryByBackupConfigId(@PathVariable("backupConfigId") String backupConfigId, Model model) {
        model.addAttribute("backupConfigId", backupConfigId);
        model.addAttribute("backupConfigDatas", backupConfigDataService.queryByBackupConfigId(backupConfigId));
        return "backupConfigData";
    }

    @GetMapping("/detail/{id}")
    public String queryById(@PathVariable("id") String id, Model model) {
        model.addAttribute("backupConfigDataVo", backupConfigDataService.queryById(id));
        return "backupConfigDataUpdate";
    }

    @PostMapping
    public String addBackupConfigData(Model model, HttpServletRequest request) {
        String backupConfigId = request.getParameter("backupConfigId");
        String cronExpr = request.getParameter("cronExpr");
        String filename = request.getParameter("filename");
        String shellCommands = request.getParameter("shellCommands");
        String[] backupPaths = request.getParameterValues("backupPath");
        String[] targetPaths = request.getParameterValues("targetPath");

        BackupConfigData backupConfigData = new BackupConfigData();
        backupConfigData.setBackupConfigId(backupConfigId);
        backupConfigData.setCronExpr(cronExpr);
        backupConfigData.setFilename(filename);
        backupConfigData.setShellCommands(shellCommands);
        backupConfigData.setBackupPaths(String.join(",", backupPaths));
        backupConfigData.setTargetPaths(String.join(",", targetPaths));


        model.addAttribute("backupConfigId", backupConfigId);
        model.addAttribute("backupConfigDatas", backupConfigDataService.addBackupConfigData(backupConfigData));
        return "backupConfigData";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") String id, Model model) {
        if (backupConfigDataService.deleteById(id) == 0) {
            return "error";
        }
        return "success";
    }

    @ResponseBody
    @PutMapping("/{id}")
    public Result modifyBackupConfigData(@PathVariable("id") String id,
                                         @RequestBody BackupConfigDataVo backupConfigDataVo) {
        backupConfigDataVo.setId(id);
        backupConfigDataService.modifyById(backupConfigDataVo);
        /*model.addAttribute("backupConfigId", backupConfigDataVo.getBackupConfigId());
        model.addAttribute("backupConfigDatas", backupConfigDataService.modifyById(backupConfigDataVo));*/
        Result result = new Result();
        result.setResult("success");
        result.setData(backupConfigDataVo.getBackupConfigId());
        return result;
    }
}
