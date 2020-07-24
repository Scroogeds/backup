package com.leateck.gmp.backup.core.mapper;

import com.leateck.gmp.backup.base.mapper.BaseMapper;
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
public interface BackupConfigMapper extends BaseMapper<BackupConfig> {

    List<BackupConfig> queryAll();

    List<BackupConfig> queryEnable(@Param("enable") String enable);

    int modifyBackupConfigEnable(@Param("code") String code, @Param("enable") String enable);

    /*List<BackupConfig> queryJavaCron(@Param("cronType") String cronType);*/

    BackupConfig queryByCode(@Param("code") String code);

    int insertBackupConfig(@Param("backupConfig") BackupConfig backupConfig);

    int modifyBackupConfigByCode(@Param("backupConfig") BackupConfig backupConfig);

    int deleteByCode(@Param("code") String code);
}
