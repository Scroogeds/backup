package com.leateck.gmp.backup.boot;

import org.springframework.boot.ApplicationArguments;

import java.io.Serializable;

/**
 * <p>Title: IBackupApplicationRunner</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
public interface IBackupApplicationRunner extends Serializable {

    void run(ApplicationArguments args);

}
