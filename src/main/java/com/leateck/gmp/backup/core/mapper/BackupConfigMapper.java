package com.leateck.gmp.backup.core.mapper;

import com.leateck.gmp.backup.core.entity.BackupConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: BackupConfigMapper</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-06   luyangqian  Created
 * </pre>
 */
@Mapper
public interface BackupConfigMapper {

    List<BackupConfig> queryAll();

    List<BackupConfig> queryByPage();

    BackupConfig queryByCode(@Param("code") String code);

    int insertBackupConfig(@Param("backupConfig") BackupConfig backupConfig);

    int modifyBackupConfigByCode(@Param("backupConfig") BackupConfig backupConfig);

    int deleteByCode(@Param("code") String code);
}
