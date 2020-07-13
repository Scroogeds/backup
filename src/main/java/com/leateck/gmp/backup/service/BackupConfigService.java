package com.leateck.gmp.backup.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.entity.BackupConfig;
import com.leateck.gmp.backup.entity.BackupConfigData;
import com.leateck.gmp.backup.mapper.BackupConfigDataMapper;
import com.leateck.gmp.backup.mapper.BackupConfigMapper;
import com.leateck.gmp.backup.utils.StringUtil;
import com.leateck.gmp.backup.vo.BackupConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <p>Title: BackupConfigService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
@Service
public class BackupConfigService implements IBackupConfigService {

    private String shellPath;

    @Value("${gmp-backup.shellPath}")
    public void setShellPath(String shellPath) {
        this.shellPath = shellPath.endsWith(File.separator) ? shellPath : shellPath + File.separator;
    }

    private BackupConfigMapper backupConfigMapper;

    private BackupConfigDataMapper backupConfigDataMapper;

    @Autowired
    public void setBackupConfigMapper(BackupConfigMapper backupConfigMapper) {
        this.backupConfigMapper = backupConfigMapper;
    }

    @Autowired
    public void setBackupConfigDataMapper(BackupConfigDataMapper backupConfigDataMapper) {
        this.backupConfigDataMapper = backupConfigDataMapper;
    }

    @Override
    public List<BackupConfig> queryBackupConfig() {
        /*List<BackupConfigVo> backupConfigs = new ArrayList<>();
        ClassPathResource classPathResource = new ClassPathResource("backupConfig.json");
        try (InputStream inputStream = classPathResource.getInputStream()){
            String backupConfigVar = IoUtil.read(inputStream, Charset.forName("utf-8"));

            JSONArray jsonArray = JSONUtil.parseArray(backupConfigVar);
            jsonArray.forEach(o ->
                backupConfigs.add(JSONUtil.toBean((JSONObject) o, BackupConfigVo.class))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return backupConfigMapper.queryAll();
    }

    private BackupConfigVo convertVo(BackupConfig backupConfig) {
        BackupConfigVo backupConfigVo = new BackupConfigVo();
        BeanUtils.copyProperties(backupConfig, backupConfigVo);
        return backupConfigVo;
    }

    /*@Override
    public BackupConfigVo queryBackupConfigById(String id) {
        ClassPathResource classPathResource = new ClassPathResource("backupConfig.json");
        try (InputStream inputStream = classPathResource.getInputStream()){
            String backupConfigVar = IoUtil.read(inputStream, Charset.forName("utf-8"));
//            int index = Integer.valueOf(id);
//            JSONArray jsonArray = JSONUtil.parseArray(backupConfigVar);
//            if (index <= jsonArray.size()) {
//                return JSONUtil.toBean((JSONObject) jsonArray.get(index), BackupConfig.class);
//            }
            int idInt = Integer.valueOf(id);
            JSONArray jsonArray = JSONUtil.parseArray(backupConfigVar);
            List<BackupConfig> backupConfigs = new ArrayList<>();
            jsonArray.forEach(o ->
                    backupConfigs.add(JSONUtil.toBean((JSONObject) o, BackupConfig.class))
            );
            //return backupConfigs.stream().filter(backupConfig -> idInt == backupConfig.getId()).findFirst().orElse(null);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    public BackupConfig queryBackupConfigById(String id) {
        /*BackupConfig backupConfig = backupConfigMapper.queryById(id);
        if (null != backupConfig) {

        }*/
        /*BackupConfigVo backupConfigVo = new BackupConfigVo();
        BeanUtils.copyProperties(backupConfig, backupConfigVo);
        List<BackupConfigData> backupConfigDatas = backupConfigDataMapper.queryByBackupConfigId(id);

        if (!CollectionUtils.isEmpty(backupConfigDatas)) {
            List<BackupConfigDataVo> backupConfigDataVos = new ArrayList<>(backupConfigDatas.size());
            backupConfigDatas.forEach(backupConfigData -> {
                BackupConfigDataVo backupConfigDataVo = new BackupConfigDataVo();
                String backupPaths = backupConfigData.getBackupPaths();
                if (!StringUtils.isEmpty(backupPaths)) {
                    backupConfigDataVo.setBackupPaths(Stream.of(backupPaths.split(","))
                            .collect(Collectors.toList()));
                }
                backupConfigDataVo.setShellCommands(backupConfigData.getShellCommands());
                backupConfigDataVos.add(backupConfigDataVo);
            });
            backupConfigVo.setBackupConfigDataVos(backupConfigDataVos);
        }*/
        return backupConfigMapper.queryById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BackupConfig> addBackupConfig(BackupConfig backupConfig) {
        backupConfig.setId(StringUtil.getId(BackupConstant.BACKUP_CONFIG_SIGN));
        backupConfigMapper.insertBackupConfig(backupConfig);
        return backupConfigMapper.queryAll();
    }

    @Override
    public int modifyBackupConfig(BackupConfig backupConfig) {
        /*BackupConfig backupConfig = new BackupConfig();
        BeanUtils.copyProperties(backupConfigVo, backupConfig);
        if (backupConfigMapper.modifyBackupConfigById(backupConfig) > 0) {
            String id = backupConfig.getId();
            backupConfigDataMapper.deleteByBackupConfigId(id);
            List<BackupConfigDataVo> backupConfigDataVos = backupConfigVo.getBackupConfigDataVos();
            if (!CollectionUtils.isEmpty(backupConfigDataVos)) {
                backupConfigDataVos
                        *//*.stream()
                        .filter(backupConfigDataVo -> !StringUtils.isEmpty(backupConfigDataVo.getShellCommands()) ||
                        !CollectionUtils.isEmpty(backupConfigDataVo.getBackupPaths()))*//*
                        .stream()
                        .filter(backupConfigDataVo -> null != backupConfigDataVo)
                        .forEach(backupConfigDataVo -> {
                            if (!StringUtils.isEmpty(backupConfigDataVo.getShellCommands()) ||
                                    !CollectionUtils.isEmpty(backupConfigDataVo.getBackupPaths())) {
                                BackupConfigData backupConfigData = new BackupConfigData();
                                BeanUtils.copyProperties(backupConfigDataVo, backupConfigData);
                                backupConfigData.setBackupConfigId(id);
                                backupConfigData.setId(StringUtil.getId(BackupConstant.BACKUP_CONFIG_DATA_SIGN));
                                List<String> backupPaths = backupConfigDataVo.getBackupPaths();
                                if (!CollectionUtils.isEmpty(backupPaths)) {
                                    backupConfigData.setBackupPaths(String.join(",", backupPaths));
                                }
                                backupConfigDataMapper.insertData(backupConfigData);
                            }
                        });
            }
        }*/
        return backupConfigMapper.modifyBackupConfigById(backupConfig);
    }

    @Override
    public int deleteBackupConfig(String id) {
        int num = backupConfigMapper.deleteById(id);
        if (num > 0) {
            backupConfigDataMapper.deleteByBackupConfigId(id);
        }
        return num;
    }

    @Override
    public void initH2DataBase() {
        ClassPathResource classPathResource = new ClassPathResource("backupConfig.json");
        try (InputStream inputStream = classPathResource.getInputStream()){
            String backupConfigVar = IoUtil.read(inputStream, Charset.forName("utf-8"));
            JSONArray jsonArray = JSONUtil.parseArray(backupConfigVar);
            jsonArray.forEach(o -> {
                BackupConfig backupConfig = JSONUtil.toBean((JSONObject) o, BackupConfig.class);
                backupConfigMapper.insertBackupConfig(backupConfig);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveH2DataBase() {
        List<BackupConfig> backupConfigs = backupConfigMapper.queryAll();
        JSONArray jsonArray = new JSONArray();
        for (BackupConfig backupConfig : backupConfigs) {
            jsonArray.add(backupConfig);
        }
        ClassPathResource classPathResource = new ClassPathResource("backupConfig.json");
        /*classPathResource.ge
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter())*/
    }

    @Override
    public String buildShellFile(String backupConfigDataId) {
        BackupConfigData backupConfigData = backupConfigDataMapper.queryById(backupConfigDataId);
        if (null != backupConfigData) {
            String filename = getShellFilename(backupConfigData.getFilename(), backupConfigDataId);
            buildShellFile(backupConfigData,
                    new File(filename));
            RuntimeUtil.execForStr("chmod 744 " + filename);
        }
        return "build success";
    }

    private String getShellFilename(String filename, String backupConfigDataId) {
        String executeShell = null;
        //String filename = backupConfigData.getFilename();
        if (!StringUtils.isEmpty(filename)) {
            executeShell = shellPath + filename;
            if (!executeShell.endsWith(".sh")) {
                executeShell = executeShell + ".sh";
            }
        } else {
            executeShell = shellPath + backupConfigDataId + ".sh";
        }
        return executeShell;
    }

    private void buildShellFile(BackupConfigData backupConfigData, File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            bufferedWriter.write("#!/bin/bash");
            bufferedWriter.newLine();
            bufferedWriter.write("cd " + shellPath);
            bufferedWriter.newLine();
            bufferedWriter.write("tmpDate=$(date \"+%Y%m%d%H%M%S\")");
            bufferedWriter.newLine();
            String shellCommands = backupConfigData.getShellCommands();
            if (!StringUtils.isEmpty(shellCommands)) {
                bufferedWriter.write(shellCommands);
                bufferedWriter.newLine();
            }
            String backupPaths = backupConfigData.getBackupPaths();
            if (!StringUtils.isEmpty(backupPaths)) {
                bufferedWriter.write("tar -zxvf " + backupConfigData.getBackupFilename() + "-$tmpDate.tar.gz " + backupPaths.replace(",", " "));
                bufferedWriter.newLine();
            }
            BackupConfig backupConfig = backupConfigMapper.queryById(backupConfigData.getBackupConfigId());
            String targetPaths = backupConfigData.getTargetPaths();
            if (!StringUtils.isEmpty(targetPaths)) {
                for (String targetPath : targetPaths.split(",")) {
                    bufferedWriter.write("sshpass -p " + backupConfig.getPassword() +
                            " scp " + backupConfigData.getBackupFilename() + "-$tmpDate.tar.gz " +
                            backupConfig.getUsername() + "@" + backupConfig.getAddress() + ":" + targetPath);
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.write("rm -rf " + backupConfigData.getBackupFilename() + "-$tmpDate.tar.gz");
        } catch (Exception e) {
            //
        }
    }

    @Override
    public String executeShell(String backupConfigDataId) {
        BackupConfigData backupConfigData = backupConfigDataMapper.queryById(backupConfigDataId);
        if (null != backupConfigData) {
            String executeShell = getShellFilename(backupConfigData.getFilename(), backupConfigDataId);
            File file = new File(executeShell);
            if (file.exists()) {
                return RuntimeUtil.execForStr(executeShell);
            } else {
                buildShellFile(backupConfigData, file);
                RuntimeUtil.execForStr("chmod 744 " + executeShell);
                return RuntimeUtil.execForStr(executeShell);
            }
        }
        return "execute success";
    }
}
