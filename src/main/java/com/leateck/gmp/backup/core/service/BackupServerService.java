package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.core.entity.BackupServer;
import com.leateck.gmp.backup.core.mapper.BackupServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>Title: BackupServerService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Service
public class BackupServerService implements IBackupServerService{

    private BackupServerMapper backupServerMapper;

    @Autowired
    public void setBackupServerMapper(BackupServerMapper backupServerMapper) {
        this.backupServerMapper = backupServerMapper;
    }

    @Override
    public List<BackupServer> queryByConfigCode(String configCode) {
        return backupServerMapper.queryByConfigCode(configCode);
    }

    @Override
    public List<BackupServer> queryByConfigCodeAndServerType(String configCode, String serverType) {
        return backupServerMapper.queryByConfigCodeAndServerType(configCode, serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBackupServer(String backupCode, List<BackupServer> sourceServers, List<BackupServer> targetServers) {
        if (!CollectionUtils.isEmpty(sourceServers)) {
            for (BackupServer sourceServer : sourceServers) {
                sourceServer.setConfigCode(backupCode);
                sourceServer.setServerType(BackupConstant.SERVER_TYPE_SOURCE_VAR);
                backupServerMapper.insertBackupServer(sourceServer);
            }
        }
        if (!CollectionUtils.isEmpty(targetServers)) {
            for (BackupServer targetServer : targetServers) {
                targetServer.setConfigCode(backupCode);
                targetServer.setServerType(BackupConstant.SERVER_TYPE_TARGET_VAR);
                backupServerMapper.insertBackupServer(targetServer);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByConfigCode(String configCode) {
        return backupServerMapper.deleteByConfigCode(configCode);
    }

    @Override
    public BackupServer queryByRowId(String rowId) {
        return backupServerMapper.queryByRowId(rowId);
    }
}
