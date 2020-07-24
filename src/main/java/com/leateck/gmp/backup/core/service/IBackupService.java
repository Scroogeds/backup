package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.core.vo.RecoverConfig;
import com.leateck.gmp.backup.core.vo.RecoverConfigVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

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

    void initEnableCron();

    Result<List<String>> queryDirFile(RecoverConfig recoverConfig);

    InputStream downFile(RecoverConfig recoverConfig, String filename);

    Result<String> uploadFile(String filename, RecoverConfigVo recoverConfigVo);

    Result<String> uploadFile(MultipartFile file, RecoverConfig recoverConfig);

}
