package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.base.entity.Result;

/**
 * <p>Title: IBackupService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-16   luyangqian  Created
 * </pre>
 */
public interface IBackupService {

    Result<String> buildShell(String code);

    Result<String> executeShell(String code);

    Result<String> buildCron(String code);

    Result<String> removeCron(String code);

}
