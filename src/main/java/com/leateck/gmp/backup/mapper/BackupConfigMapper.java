package com.leateck.gmp.backup.mapper;

import com.leateck.gmp.backup.entity.BackupConfig;
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

    BackupConfig queryById(@Param("id") String id);

    int insertBackupConfig(@Param("backConfig") BackupConfig backupConfig);

    int modifyBackupConfigById(@Param("backConfig") BackupConfig backupConfig);

    int deleteById(@Param("id") String id);
}
