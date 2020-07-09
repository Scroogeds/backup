package com.leateck.gmp.backup.entity;

import com.leateck.gmp.backup.constant.BackupConstant;
import lombok.Data;

/**
 * <p>Title: BackupConfig</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Data
public class BackupConfig {

    private String id;

    private String sysType;

    private String address;

    private String username;

    private String password;

    private String connectType = BackupConstant.DEFAULT_CONNECT_TYPE_VAR;

}
