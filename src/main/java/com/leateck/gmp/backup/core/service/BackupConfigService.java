package com.leateck.gmp.backup.core.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leateck.gmp.backup.base.Result;
import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.entity.BackupServer;
import com.leateck.gmp.backup.core.mapper.BackupConfigMapper;
import com.leateck.gmp.backup.core.vo.BackupConfigVo;
import com.leateck.gmp.backup.exception.BackupException;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;
import com.leateck.gmp.backup.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: BackupConfigService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Service
public class BackupConfigService implements IBackupConfigService {

    private String shellPath;

    @Value("${gmp-backup.shellPath}")
    public void setShellPath(String shellPath) {
        this.shellPath = shellPath.endsWith(File.separator) ? shellPath : shellPath + File.separator;
    }

    private BackupConfigMapper backupConfigMapper;

    private IBackupServerService backupServerService;

    @Autowired
    public void setBackupConfigMapper(BackupConfigMapper backupConfigMapper) {
        this.backupConfigMapper = backupConfigMapper;
    }

    @Autowired
    public void setBackupServerService(IBackupServerService backupServerService) {
        this.backupServerService = backupServerService;
    }

    @Override
    public List<BackupConfig> queryAll() {
        return backupConfigMapper.queryAll();
    }

    @Override
    public Result<PageData<BackupConfig>> queryByPage(SearchParamWrapper wrapper) {
        int pageNum = wrapper.getPageNum();
        int pageSize = wrapper.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<BackupConfig> backupConfigs = backupConfigMapper.queryByPage();
        return new Result<>(PageUtil.getPageData(new PageInfo<>(backupConfigs)));
    }

    @Override
    public Result<BackupConfigVo> queryByCode(String code) {
        BackupConfigVo backupConfigVo = new BackupConfigVo();
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            BeanUtils.copyProperties(backupConfig, backupConfigVo);
            List<BackupServer> backupServers = backupServerService.queryByConfigCode(code);
            if (!CollectionUtils.isEmpty(backupServers)) {
                List<BackupServer> sourceServers = new ArrayList<>();
                List<BackupServer> targetServers = new ArrayList<>();
                for (BackupServer backupServer : backupServers) {
                    if (BackupConstant.SERVER_TYPE_SOURCE_VAR.equals(backupServer.getServerType())) {
                        sourceServers.add(backupServer);
                    } else if (BackupConstant.SERVER_TYPE_TARGET_VAR.equals(backupServer.getServerType())) {
                        targetServers.add(backupServer);
                    }
                }
                if (!CollectionUtils.isEmpty(sourceServers)) {
                    backupConfigVo.setSourceServers(sourceServers);
                }
                if (!CollectionUtils.isEmpty(targetServers)) {
                    backupConfigVo.setTargetServers(targetServers);
                }
            }
        }
        return new Result<>(backupConfigVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<BackupConfigVo> addBackupConfig(BackupConfigVo backupConfigVo) {
        List<BackupServer> sourceServers = backupConfigVo.getSourceServers();
        List<BackupServer> targetServers = backupConfigVo.getTargetServers();
        if (CollectionUtils.isEmpty(sourceServers) && CollectionUtils.isEmpty(targetServers)) {
            throw new BackupException(100001, "源系统和目标系统不能都为空", backupConfigVo);
        }

        BackupConfig backupConfig = new BackupConfig();
        BeanUtils.copyProperties(backupConfigVo, backupConfig);
        String code = IdUtil.simpleUUID();
        backupConfig.setCode(code);
        if (backupConfigMapper.insertBackupConfig(backupConfig) > 0) {
            backupServerService.addBackupServer(code, sourceServers, targetServers);
        }
        return new Result<>(backupConfigVo);
    }

    @Override
    public Result<BackupConfigVo> modifyBackupConfig(BackupConfigVo backupConfigVo) {
        List<BackupServer> sourceServers = backupConfigVo.getSourceServers();
        List<BackupServer> targetServers = backupConfigVo.getTargetServers();
        if (CollectionUtils.isEmpty(sourceServers) && CollectionUtils.isEmpty(targetServers)) {
            throw new BackupException(100001, "源系统和目标系统不能都为空", backupConfigVo);
        }
        BackupConfig backupConfig = new BackupConfig();
        BeanUtils.copyProperties(backupConfigVo, backupConfig);
        if (backupConfigMapper.modifyBackupConfigByCode(backupConfig) > 0) {
            String code = backupConfig.getCode();
            backupServerService.deleteByConfigCode(code);
            backupServerService.addBackupServer(code, sourceServers, targetServers);
        }
        return new Result<>(backupConfigVo);
    }

    @Override
    public Result<String> deleteByCode(String code) {
        if (backupConfigMapper.deleteByCode(code) > 0) {
            backupServerService.deleteByConfigCode(code);
        }
        return new Result<>(code);
    }
}