package com.leateck.gmp.backup.core.mapper;

import com.leateck.gmp.backup.core.entity.BackupServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: BackupServerMapper</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-14   luyangqian  Created
 * </pre>
 */
@Mapper
public interface BackupServerMapper {

    List<BackupServer> queryByConfigCode(@Param("configCode") String configCode);

    BackupServer queryByRowId(@Param("rowId") String rowId);

    int insertBackupServer(@Param("backupServer") BackupServer backupServer);

    int deleteByConfigCode(@Param("configCode") String configCode);
}
