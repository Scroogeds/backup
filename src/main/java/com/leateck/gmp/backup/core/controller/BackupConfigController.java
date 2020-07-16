package com.leateck.gmp.backup.core.controller;

import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.service.IBackupConfigService;
import com.leateck.gmp.backup.core.vo.BackupConfigVo;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "备份信息基本管理", tags = {"备份信息基本管理"})
@RestController
@RequestMapping("/backup/config")
public class BackupConfigController {

    private IBackupConfigService backupConfigService;

    @Autowired
    public void setBackupConfigService(IBackupConfigService backupConfigService) {
        this.backupConfigService = backupConfigService;
    }

    @ApiOperation(value = "获取备份配置信息，不分页", notes = "获取备份配置信息，不分页")
    @GetMapping("/all")
    public Result<List<BackupConfig>> queryAll() {
        return new Result<>(backupConfigService.queryAll());
    }

    @ApiOperation(value = "获取备份配置信息，支持分页", notes = "获取备份配置信息，支持分页")
    @GetMapping()
    public Result<PageData<BackupConfig>> queryByPage(SearchParamWrapper wrapper) {
        return backupConfigService.queryByPage(wrapper);
    }

    @ApiOperation(value = "获取备份配置信息，根据CODE", notes = "根据CODE获取备份配置信息")
    @ApiImplicitParam(paramType = "path", name = "code", value = "备份编号", required = true, dataType = "String")
    @GetMapping("/{code}")
    public Result<BackupConfigVo> queryByCode(@PathVariable("code") String code) {
        return backupConfigService.queryByCode(code);
    }

    @ApiOperation(value = "新增备份配置信息", notes = "新增备份配置信息")
    @PostMapping()
    public Result<BackupConfigVo> addBackupConfigVo(@RequestBody BackupConfigVo backupConfigVo) {
        return backupConfigService.addBackupConfig(backupConfigVo);
    }

    @ApiOperation(value = "根据编号修改备份配置信息", notes = "根据编号修改备份配置信息")
    @PutMapping("/{code}")
    public Result<BackupConfigVo> modifyBackupConfig(@PathVariable("code") String code, @RequestBody BackupConfigVo backupConfigVo) {
        backupConfigVo.setCode(code);
        return backupConfigService.modifyBackupConfig(backupConfigVo);
    }

    @ApiOperation(value = "根据编号删除备份配置信息", notes = "根据编号删除备份配置信息")
    @DeleteMapping("/{code}")
    public Result<String> deleteByCode(@PathVariable("code") String code) {
        return backupConfigService.deleteByCode(code);
    }


}
