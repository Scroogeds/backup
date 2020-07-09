package com.leateck.gmp.backup.vo;

import com.leateck.gmp.backup.entity.BackupConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: BackupConfigVo</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
@Data
public class BackupConfigVo extends BackupConfig implements Serializable {

    private static final long serialVersionUID = 1753436637566862826L;

    private List<BackupConfigDataVo> backupConfigDataVos;
}
