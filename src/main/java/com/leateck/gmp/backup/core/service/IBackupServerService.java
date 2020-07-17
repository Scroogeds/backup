package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.core.entity.BackupServer;

import java.util.List;

/**
 * <p>Title: IBackupServerService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
public interface IBackupServerService {

    List<BackupServer> queryByConfigCode(String configCode);

    List<BackupServer> queryByConfigCodeAndServerType(String configCode, String serverType);

    void addBackupServer(String backupCode, List<BackupServer> sourceServers, List<BackupServer> targetServers);

    int deleteByConfigCode(String configCode);
}
