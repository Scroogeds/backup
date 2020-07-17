package com.leateck.gmp.backup.core.service;

import cn.hutool.core.util.RuntimeUtil;
import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.entity.BackupServer;
import com.leateck.gmp.backup.core.mapper.BackupConfigMapper;
import com.leateck.gmp.backup.exception.BackupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * <p>Title: BackupService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-16   luyangqian  Created
 * </pre>
 */
@Service
public class BackupService implements IBackupService {

    private String baseShellPath;

    @Value("${gmp-backup.base-shell-path}")
    public void setShellPath(String baseShellPath) {
        this.baseShellPath = baseShellPath.endsWith(File.separator) ? baseShellPath : baseShellPath + File.separator;
    }

    private BackupConfigMapper backupConfigMapper;

    private IBackupServerService backupServerService;

    @Autowired
    public void setBackupConfigMapper(BackupConfigMapper backupConfigMapper) {
        this.backupConfigMapper = backupConfigMapper;
    }

    @Autowired
    public void setBackupServerService(IBackupServerService backupServerService) {
        this.backupServerService = backupServerService;
    }

    @Override
    public Result<String> buildShell(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(backupConfig.getCronType())) {
                throw new BackupException(100002, "配置类型不正确，不能生成脚本文件", code);
            }
            String filename = getShellFilename(code);
            buildShellFile(code, new File(filename));
            RuntimeUtil.execForStr("chmod 744 " + filename);
        }
        return new Result<>(code);
    }

    /**
     * 拼接shell脚本名
     * @param code 编号
     * @return
     */
    private String getShellFilename(String code) {
        return baseShellPath + code + ".sh";
    }

    /**
     * 构建文件
     * @param code 编号
     * @param file 文件
     */
    private void buildShellFile(String code, File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            bufferedWriter.write("#!/bin/bash");
            bufferedWriter.newLine();

            //进入目录
            bufferedWriter.write("cd " + baseShellPath);
            bufferedWriter.newLine();

            //构建备份目录
            bufferedWriter.write("mkdir backup");
            bufferedWriter.newLine();

            //添加源服务器信息
            List<BackupServer> sourceServers = backupServerService
                    .queryByConfigCodeAndServerType(code, BackupConstant.SERVER_TYPE_SOURCE_VAR);
            if (!CollectionUtils.isEmpty(sourceServers)) {
                for (BackupServer sourceServer : sourceServers) {
                    if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(sourceServer.getConnectType())) {
                        String localhostPath = "127.0.0.1";
                        bufferedWriter.write("mkdir backup/" + localhostPath);
                        bufferedWriter.newLine();

                        String filepath = sourceServer.getFilepath();
                        if (filepath.contains(",")) {
                            for (String path : filepath.split(",")) {
                                bufferedWriter.write("cp -r -f --parents " + path + " backup/" + localhostPath);
                                bufferedWriter.newLine();
                            }
                        } else {
                            bufferedWriter.write("cp -r -f --parents " + filepath + " backup/" + localhostPath);
                            bufferedWriter.newLine();
                        }
                    } else {
                        String address = sourceServer.getAddress();
                        bufferedWriter.write("mkdir backup/" + address);
                        bufferedWriter.newLine();
                        String filepath = sourceServer.getFilepath();
                        if (filepath.contains(",")) {
                            bufferedWriter.write("sshpass -p "+ sourceServer.getPassword() +
                                    " scp -o StrictHostKeyChecking=no -P " + sourceServer.getPort() +
                                    "-r " + sourceServer.getUsername() + "@" + address + ":" +
                                    "\\{" + filepath + "\\} backup/" + address);
                        } else {
                            bufferedWriter.write("sshpass -p "+ sourceServer.getPassword() +
                                    " scp -o StrictHostKeyChecking=no -P " + sourceServer.getPort() +
                                    "-r " + sourceServer.getUsername() + "@" + address + ":" +
                                    filepath + " backup/" + address);
                        }
                        bufferedWriter.newLine();
                    }
                }

                //获取当前时间
                bufferedWriter.write("tmpDate=$(date \"+%Y%m%d%H%M%S\")");
                bufferedWriter.newLine();

                //进行压缩
                bufferedWriter.write("tar -zxvf backup-$tmpDate.tar.gz backup");
                bufferedWriter.newLine();

                //备份到指定的服务器的目录
                List<BackupServer> targetServers = backupServerService
                        .queryByConfigCodeAndServerType(code, BackupConstant.SERVER_TYPE_TARGET_VAR);
                if (!CollectionUtils.isEmpty(targetServers)) {
                    for (BackupServer targetServer : targetServers) {
                        if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(targetServer.getConnectType())) {
                            String filepath = targetServer.getFilepath();
                            if (filepath.contains(",")) {
                                for (String path : filepath.split(",")) {
                                    bufferedWriter.write("cp -r -f backup-$tmpDate.tar.gz " + path);
                                    bufferedWriter.newLine();
                                }
                            } else {
                                bufferedWriter.write("cp -r -f backup-$tmpDate.tar.gz " + filepath);
                                bufferedWriter.newLine();
                            }
                        } else {
                            String filepath = targetServer.getFilepath();
                            String address = targetServer.getAddress();
                            String password = targetServer.getPassword();
                            String port = targetServer.getPort();
                            String username = targetServer.getUsername();
                            if (filepath.contains(",")) {
                                for (String path : filepath.split(",")) {
                                    bufferedWriter.write("sshpass -p "+ password +
                                            " scp -o StrictHostKeyChecking=no -P " + port +
                                            "-r backup-$tmpDate.tar.gz " + username + "@" + address +
                                            ":" + path);
                                    bufferedWriter.newLine();
                                }
                            } else {
                                bufferedWriter.write("sshpass -p "+ password +
                                        " scp -o StrictHostKeyChecking=no -P " + port +
                                        "-r backup-$tmpDate.tar.gz " + username + "@" + address +
                                        ":" + filepath);
                                bufferedWriter.newLine();
                            }

                        }
                    }
                }

                //删除掉中间文件
                bufferedWriter.write("rm -rf backup backup-$tmpDate.tar.gz");
                bufferedWriter.newLine();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result<String> executeShell(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(backupConfig.getCronType())) {
                String filename = getShellFilename(code);
                File file = new File(filename);
                if (file.exists()) {
                    return new Result<>(RuntimeUtil.execForStr(filename));
                } else {
                    buildShellFile(code, file);
                    RuntimeUtil.execForStr("chmod 744 " + filename);
                    return new Result<>(RuntimeUtil.execForStr(filename));
                }
            } else if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(backupConfig.getCronType())){
                //todo cronjavade
            }

        }
        return new Result<>(code);
    }

    @Override
    public Result<String> buildCron(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(backupConfig.getCronType())) {
                throw new BackupException(100003, "配置类型不正确，不能加入Linux系统定时任务", code);
            }
            String filename = getShellFilename(code);
            File file = new File(filename);
            if (!file.exists()) {
                buildShellFile(code, file);
                RuntimeUtil.execForStr("chmod 744 " + filename);
            }
            String exec = RuntimeUtil.execForStr("echo \"" + backupConfig.getCronExpr() + " " +
                    filename + "\" >> /var/spool/cron/root");
            return new Result<>(exec);
        }
        return new Result<>(code);
    }

    @Override
    public Result<String> removeCron(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(backupConfig.getCronType())) {
                throw new BackupException(100004, "配置类型不正确，不能删除Linux系统定时任务", code);
            }
            String filename = getShellFilename(code);
            String exec = RuntimeUtil.execForStr("sed i \"/" + filename + "/d\" /var/spool/cron/root");
            return new Result<>(exec);
        }
        return new Result<>(code);
    }
}
