package com.leateck.gmp.backup.core.service;

import com.leateck.gmp.backup.base.Result;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.vo.BackupConfigVo;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;

import java.util.List;

/**
 * <p>Title: IBackupConfigService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
public interface IBackupConfigService {

    List<BackupConfig> queryAll();

    Result<PageData<BackupConfig>> queryByPage(SearchParamWrapper wrapper);

    Result<BackupConfigVo> queryByCode(String code);

    Result<BackupConfigVo> addBackupConfig(BackupConfigVo backupConfigVo);

    Result<BackupConfigVo> modifyBackupConfig(BackupConfigVo backupConfigVo);

    Result<String> deleteByCode(String code);
}
