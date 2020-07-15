package com.leateck.gmp.backup.core.entity;

import com.leateck.gmp.backup.constant.BackupConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>Title: BackupServer</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-13   luyangqian  Created
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class BackupServer extends BaseData {

    private String configCode;

    /**
     * 服务器类型 0-linux 1-wins
     */
    private String sysType = BackupConstant.DEFAULT_SYS_TYPE;

    /**
     * IP地址
     */
    private String address;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接方式
     */
    private String connectType = BackupConstant.DEFAULT_SSH_TYPE_VAR;

    /**
     * 端口
     */
    private String port = BackupConstant.DEFAULT_SORT;

    /**
     * 服务器类别 0-源 1-目标
     */
    private String serverType;

    /**
     * 路径
     */
    private String filepath;

    /**
     * 保存天数 -1不删除
     */
    private int saveDayNum = -1;

}
