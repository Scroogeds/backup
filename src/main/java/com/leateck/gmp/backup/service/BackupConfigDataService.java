package com.leateck.gmp.backup.service;

import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.entity.BackupConfigData;
import com.leateck.gmp.backup.mapper.BackupConfigDataMapper;
import com.leateck.gmp.backup.utils.StringUtil;
import com.leateck.gmp.backup.vo.BackupConfigDataVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Title: BackupConfigDataService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
@Service
public class BackupConfigDataService implements IBackupConfigDataService {

    private BackupConfigDataMapper backupConfigDataMapper;

    @Autowired
    public void setBackupConfigDataMapper(BackupConfigDataMapper backupConfigDataMapper) {
        this.backupConfigDataMapper = backupConfigDataMapper;
    }

    @Override
    public List<BackupConfigData> queryByBackupConfigId(String backupConfigId) {
        return backupConfigDataMapper.queryByBackupConfigId(backupConfigId);
    }

    @Override
    public List<BackupConfigData> addBackupConfigData(BackupConfigData backupConfigData) {
        backupConfigData.setId(StringUtil.getId(BackupConstant.BACKUP_CONFIG_DATA_SIGN));
        backupConfigDataMapper.insertData(backupConfigData);
        return queryByBackupConfigId(backupConfigData.getBackupConfigId());
    }

    @Override
    public int modifyById(BackupConfigDataVo backupConfigDataVo) {
        BackupConfigData backupConfigData = new BackupConfigData();
        BeanUtils.copyProperties(backupConfigDataVo, backupConfigData);
        backupConfigData.setBackupPaths(String.join(",", backupConfigDataVo.getBackupPaths()));
        backupConfigData.setTargetPaths(String.join(",", backupConfigDataVo.getTargetPaths()));
        return backupConfigDataMapper.modifyDataById(backupConfigData);
    }

    @Override
    public BackupConfigDataVo queryById(String id) {
        return new BackupConfigDataVo(backupConfigDataMapper.queryById(id));
    }

    @Override
    public int deleteById(String id) {
        return backupConfigDataMapper.deleteById(id);
    }
}
