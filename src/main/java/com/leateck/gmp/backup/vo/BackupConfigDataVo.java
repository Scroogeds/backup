package com.leateck.gmp.backup.vo;

import com.leateck.gmp.backup.entity.BackupConfigData;
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
public class BackupConfigDataVo implements Serializable {

    private static final long serialVersionUID = -2847801750405373854L;

    private String id;

    private String backupConfigId;

    private String shellCommands;

    private List<String> backupPaths;

    private String cronExpr;

    private String filename;

    private List<String> targetPaths;

    public BackupConfigDataVo(BackupConfigData backupConfigData) {
        this.id = backupConfigData.getId();
        this.backupConfigId = backupConfigData.getBackupConfigId();
        this.shellCommands = backupConfigData.getShellCommands();
        this.backupPaths = Stream.of(backupConfigData.getBackupPaths().split(","))
                .collect(Collectors.toList());
        this.cronExpr = backupConfigData.getCronExpr();
        this.filename = backupConfigData.getFilename();
        this.targetPaths = Stream.of(backupConfigData.getTargetPaths().split(","))
                .collect(Collectors.toList());
    }
}
