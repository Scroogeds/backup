package com.leateck.gmp.backup.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: RecoverConfig</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-21   luyangqian  Created
 * </pre>
 */
@Data
public class RecoverConfig implements Serializable {

    private static final long serialVersionUID = 4972941051194415449L;

    private String serverCode;

    private String recoverDir;

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

}
