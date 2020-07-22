package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.core.vo.RecoverConfig;

import java.io.InputStream;
import java.util.Map;

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

    void initJavaCron();

    Result<Map<String, Object>> queryDirFile(RecoverConfig recoverConfig);

    InputStream downFile(String cacheId, String filename);

    Result<String> uploadFile(String cacheId, String filename, RecoverConfig recoverConfig);

}
