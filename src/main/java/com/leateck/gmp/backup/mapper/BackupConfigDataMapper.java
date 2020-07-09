package com.leateck.gmp.backup.mapper;

import com.leateck.gmp.backup.entity.BackupConfigData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: BackupConfigDataMapper</p>
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
public interface BackupConfigDataMapper {

    BackupConfigData queryById(@Param("id") String id);

    List<BackupConfigData> queryByBackupConfigId(@Param("backupConfigId") String backupConfigId);

    int insertData(@Param("backupConfigData") BackupConfigData backupConfigData);

    int modifyDataById(@Param("backupConfigData") BackupConfigData backupConfigData);

    int deleteById(@Param("id") String id);

    int deleteByBackupConfigId(@Param("backupConfigId") String backupConfigId);

}
