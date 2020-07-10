package com.leateck.gmp.backup.vo;

import com.leateck.gmp.backup.entity.BackupConfigData;
import com.leateck.gmp.backup.entity.BaseBackupConfigData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Title: BackupConfigDataVo</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
@NoArgsConstructor
@Data
public class BackupConfigDataVo extends BaseBackupConfigData implements Serializable {

    private static final long serialVersionUID = -2847801750405373854L;

    private List<String> backupPaths;

    private List<String> targetPaths;

    public BackupConfigDataVo(BackupConfigData backupConfigData) {
        this.setId(backupConfigData.getId());
        this.setBackupConfigId(backupConfigData.getBackupConfigId());
        this.setShellCommands(backupConfigData.getShellCommands());
        this.backupPaths = Stream.of(backupConfigData.getBackupPaths().split(","))
                .collect(Collectors.toList());
        this.setCronExpr(backupConfigData.getCronExpr());
        this.setFilename(backupConfigData.getFilename());
        this.targetPaths = Stream.of(backupConfigData.getTargetPaths().split(","))
                .collect(Collectors.toList());
        this.setBackupFilename(backupConfigData.getBackupFilename());
        this.setSaveDayNum(backupConfigData.getSaveDayNum());
    }
}
