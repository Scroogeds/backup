package com.leateck.gmp.backup.core.vo;

import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.entity.BackupServer;
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
 *          2020-07-14   luyangqian  Created
 * </pre>
 */
@Data
public class BackupConfigVo extends BackupConfig implements Serializable {

    private static final long serialVersionUID = -7579931924203415026L;

    private List<BackupServer> sourceServers;

    private List<BackupServer> targetServers;

}
