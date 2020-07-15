package com.leateck.gmp.backup.core.controller;

import com.leateck.gmp.backup.base.Result;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.service.IBackupConfigService;
import com.leateck.gmp.backup.core.vo.BackupConfigVo;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Title: BackupConfigController</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@RestController
@RequestMapping("/backup/config")
public class BackupConfigController {

    private IBackupConfigService backupConfigService;

    @Autowired
    public void setBackupConfigService(IBackupConfigService backupConfigService) {
        this.backupConfigService = backupConfigService;
    }

    @GetMapping("/all")
    public Result<List<BackupConfig>> queryAll() {
        return new Result<>(backupConfigService.queryAll());
    }

    @GetMapping()
    public Result<PageData<BackupConfig>> queryByPage(SearchParamWrapper wrapper) {
        return backupConfigService.queryByPage(wrapper);
    }

    @GetMapping("/{code}")
    public Result<BackupConfigVo> queryByCode(@PathVariable("code") String code) {
        return backupConfigService.queryByCode(code);
    }

    @PostMapping()
    public Result<BackupConfigVo> addBackupConfigVo(@RequestBody BackupConfigVo backupConfigVo) {
        return backupConfigService.addBackupConfig(backupConfigVo);
    }

    @PutMapping("/{code}")
    public Result<BackupConfigVo> addBackupConfigVo(@PathVariable("code") String code, @RequestBody BackupConfigVo backupConfigVo) {
        backupConfigVo.setCode(code);
        return backupConfigService.modifyBackupConfig(backupConfigVo);
    }

    @DeleteMapping("/{code}")
    public Result<String> deleteByCode(@PathVariable("code") String code) {
        return backupConfigService.deleteByCode(code);
    }


}
