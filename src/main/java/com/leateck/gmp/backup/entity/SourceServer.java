package com.leateck.gmp.backup.entity;

import com.leateck.gmp.backup.constant.BackupConstant;
import lombok.Builder;
import lombok.Data;

/**
 * <p>Title: SourceServer</p>
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
@Builder(toBuilder = true)
public class SourceServer extends BaseData {

    private String backupCode;

    /**
     * 服务器类型 0-linux 1-wins
     */
    private String sysType;

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
    private String connectType;

    /**
     * 端口
     */
    private String port;

    /**
     * 服务器类别 0-源 1-目标
     */
    private String serverType;

    /**
     * 路径
     */
    private String filepath;

}
