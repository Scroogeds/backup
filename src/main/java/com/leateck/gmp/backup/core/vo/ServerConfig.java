package com.leateck.gmp.backup.core.vo;

import lombok.Data;

/**
 * <p>Title: ServerConfig</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-22   luyangqian  Created
 * </pre>
 */
@Data
public class ServerConfig {

    private String connectType;

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

    private String port;

    private String recoverDir;
}
