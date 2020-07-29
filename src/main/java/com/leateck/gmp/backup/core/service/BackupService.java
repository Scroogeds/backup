package com.leateck.gmp.backup.core.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ssh.Sftp;
import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.constant.BackupConstant;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.entity.BackupServer;
import com.leateck.gmp.backup.core.mapper.BackupConfigMapper;
import com.leateck.gmp.backup.core.vo.RecoverConfig;
import com.leateck.gmp.backup.core.vo.RecoverConfigVo;
import com.leateck.gmp.backup.core.vo.ServerConfig;
import com.leateck.gmp.backup.exception.BackupException;
import com.leateck.gmp.backup.utils.IoUtils;
import com.leateck.gmp.backup.utils.RemoteRuntimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String cronType;

    @Value("${gmp-backup.base-shell-path}")
    public void setShellPath(String baseShellPath) {
        this.baseShellPath = baseShellPath.endsWith(File.separator) ? baseShellPath.substring(0, baseShellPath.length() - 1) : baseShellPath;
    }

    @Value("${gmp-backup.cronType}")
    public void setCronType(String cronType) {
        this.cronType = cronType;
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

    /**
     * 过期时间 5分钟
     */
    //private static final long EXPIRE_TIME = 1000 * 60 * 5;

    /**
     * 带过期时间的集合
     */
    //TimedCache<String, ServerConfig> recoverConfigTimedCache = CacheUtil.newTimedCache(EXPIRE_TIME);

    @Override
    public Result<String> buildShell(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(cronType)) {
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
        return baseShellPath + File.separator + code + ".sh";
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
                                    " -r " + sourceServer.getUsername() + "@" + address + ":" +
                                    "\\{" + filepath + "\\} backup/" + address);
                        } else {
                            bufferedWriter.write("sshpass -p "+ sourceServer.getPassword() +
                                    " scp -o StrictHostKeyChecking=no -P " + sourceServer.getPort() +
                                    " -r " + sourceServer.getUsername() + "@" + address + ":" +
                                    filepath + " backup/" + address);
                        }
                        bufferedWriter.newLine();
                    }
                }

                //获取当前时间
                bufferedWriter.write("tmpDate=$(date \"+%Y%m%d%H%M%S\")");
                bufferedWriter.newLine();

                //进行压缩
                bufferedWriter.write("tar -zcvf backup-$tmpDate.tar.gz backup");
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
                                            " backup-$tmpDate.tar.gz " + username + "@" + address +
                                            ":" + path);
                                    bufferedWriter.newLine();
                                    bufferedWriter.write("sshpass -p "+ password +
                                            " ssh -p " + port + " " + username + "@" + address +
                                            "\"find "+ path +" -mtime +" + targetServer.getSaveDayNum() +
                                            " -name \"backup-*.tar.gz\" -exec rm -rf {} \\;\"");
                                    bufferedWriter.newLine();
                                }
                            } else {
                                bufferedWriter.write("sshpass -p "+ password +
                                        " scp -o StrictHostKeyChecking=no -P " + port +
                                        " backup-$tmpDate.tar.gz " + username + "@" + address +
                                        ":" + filepath);
                                bufferedWriter.newLine();
                                bufferedWriter.write("sshpass -p "+ password +
                                        " ssh -p " + port + " " + username + "@" + address +
                                        " \"find "+ filepath +" -mtime +" + targetServer.getSaveDayNum() +
                                        " -name \"backup-*.tar.gz\" -exec rm -rf {} \\;\"");
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

    private void nonLinuxExecuteShell(String code) {
        //添加源服务器信息
        List<BackupServer> sourceServers = backupServerService
                .queryByConfigCodeAndServerType(code, BackupConstant.SERVER_TYPE_SOURCE_VAR);
        if (!CollectionUtils.isEmpty(sourceServers)) {
            String cacheFilePath = baseShellPath + File.separator + "backup";
            FileUtil.mkdir(cacheFilePath);
            for (BackupServer sourceServer : sourceServers) {
                String filepath = sourceServer.getFilepath();
                if (!StringUtils.isEmpty(filepath)) {

                    String[] sourcePaths = filepath.split(",");
                    if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(sourceServer.getConnectType())) {
                        String sourceCopyPath = cacheFilePath + File.separator + "127.0.0.1";
                        FileUtil.mkdir(sourceCopyPath);
                        for (String sourcePath : sourcePaths) {
                            FileUtil.copy(sourcePath, sourceCopyPath, true);
                        }
                    } else if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(sourceServer.getConnectType())) {
                        String sourceCopyPath = cacheFilePath + File.separator + sourceServer.getAddress();
                        FileUtil.mkdir(sourceCopyPath);
                        //登录（帐号密码的SSH服务器）
                        Sftp sftp = new Sftp(sourceServer.getAddress(), Integer.valueOf(sourceServer.getPort()),
                                sourceServer.getUsername(), sourceServer.getPassword());
                        for (String sourcePath : sourcePaths) {
                            String[] split = sourcePath.split("##");
                            if (split.length == 2 && split[2].equals("d")) {
                                sftp.recursiveDownloadFolder(sourcePath, FileUtil.file(sourceCopyPath));
                            } else {
                                //下载远程文件
                                sftp.download(sourcePath, FileUtil.file(sourceCopyPath));
                            }
                        }
                    } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(sourceServer.getConnectType())) {
                        String sourceCopyPath = cacheFilePath + File.separator + sourceServer.getAddress();
                        FileUtil.mkdir(sourceCopyPath);
                        //登录（帐号密码的FTP服务器）
                        Ftp ftp = new Ftp(sourceServer.getAddress(), Integer.valueOf(sourceServer.getPort()),
                                sourceServer.getUsername(), sourceServer.getPassword());
                        for (String sourcePath : sourcePaths) {
                            String[] split = sourcePath.split("##");
                            if (split.length == 2 && split[2].equals("d")) {
                                ftp.recursiveDownloadFolder(sourcePath, FileUtil.file(sourceCopyPath));
                            } else {
                                //下载远程文件
                                ftp.download(sourcePath, FileUtil.file(sourceCopyPath));
                            }
                        }
                    }
                }

            }
            String fileCompressName = cacheFilePath + "-" +
                    DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + ".zip";
            ZipUtil.zip(cacheFilePath, fileCompressName);
            //备份到指定的服务器的目录
            List<BackupServer> targetServers = backupServerService
                    .queryByConfigCodeAndServerType(code, BackupConstant.SERVER_TYPE_TARGET_VAR);
            if (!CollectionUtils.isEmpty(targetServers)) {
                for (BackupServer targetServer : targetServers) {
                    String filepath = targetServer.getFilepath();
                    if (!StringUtils.isEmpty(filepath)) {
                        String[] targetPaths = filepath.split(",");
                        if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(targetServer.getConnectType())) {
                            long nowCurrentTimeMillis = System.currentTimeMillis();
                            int saveDayNum = targetServer.getSaveDayNum();
                            for (String targetPath : targetPaths) {
                                FileUtil.copy(fileCompressName, targetPath, true);
                                IoUtils.deleteOverTimeFile(nowCurrentTimeMillis, new File(targetPath), saveDayNum);
                            }
                        } else {
                            if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(targetServer.getConnectType())){
                                //登录（帐号密码的SSH服务器）
                                Sftp sftp = new Sftp(targetServer.getAddress(), Integer.valueOf(targetServer.getPort()),
                                        targetServer.getUsername(), targetServer.getPassword());
                                for (String targetPath : targetPaths) {
                                    //上传本地文件
                                    sftp.upload(targetPath, FileUtil.file(fileCompressName));
                                }
                            } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(targetServer.getConnectType())) {
                                //登录（帐号密码的FTP服务器）
                                Ftp ftp = new Ftp(targetServer.getAddress(), Integer.valueOf(targetServer.getPort()),
                                        targetServer.getUsername(), targetServer.getPassword());
                                for (String targetPath : targetPaths) {
                                    //上传本地文件
                                    ftp.upload(targetPath, FileUtil.file(fileCompressName));
                                }
                            }
                            String[] rmDirCmds = new String[targetPaths.length];
                            int saveDayNum = targetServer.getSaveDayNum();
                            //删除超过日期的备份文件
                            for (int i = 0; i < targetPaths.length; i++) {
                                rmDirCmds[i] = "find "+ targetPaths[i] +" -mtime +" + saveDayNum +
                                        " -name \"backup-*.zip\" -exec rm -rf {} \\";
                            }
                            RemoteRuntimeUtil.execForLines(targetServer.getAddress(), Integer.valueOf(targetServer.getPort()),
                                    targetServer.getUsername(), targetServer.getPassword(), rmDirCmds);
                        }
                    }
                }
            }
            FileUtil.del(fileCompressName);
            FileUtil.del(cacheFilePath);
        }
    }

    @Override
    public Result<String> executeShell(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                String filename = getShellFilename(code);
                File file = new File(filename);
                if (file.exists()) {
                    return new Result<>(RuntimeUtil.execForStr(filename));
                } else {
                    buildShellFile(code, file);
                    RuntimeUtil.execForStr("chmod 744 " + filename);
                    return new Result<>(RuntimeUtil.execForStr(filename));
                }
            } else if (BackupConstant.DEFAULT_CORN_EXPR_TYPE_JAVA.equals(cronType)){
                //java代码方式执行
                nonLinuxExecuteShell(code);
            }

        }
        return new Result<>(code);
    }

    @Override
    public Result<String> buildCron(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        String exec = code;
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                /*throw new BackupException(100003, "配置类型不正确，不能加入Linux系统定时任务", code);*/
                String filename = getShellFilename(code);
                File file = new File(filename);
                /*if (!file.exists()) {
                    buildShellFile(code, file);
                    RuntimeUtil.execForStr("chmod 744 " + filename);
                }*/
                buildShellFile(code, file);
                RuntimeUtil.execForStr("chmod 744 " + filename);

                try {
                    RuntimeUtil.execForStr("sed -i /" + code + ".sh/d /var/spool/cron/root");
                } catch (IORuntimeException e) {
                    e.printStackTrace();
                }

                File cronRootFile = new File("/var/spool/cron/root");
                FileAppender appender = new FileAppender(cronRootFile, 16, true);
                appender.append(backupConfig.getCronExpr() + " " + filename);
                appender.flush();

                /*exec = RuntimeUtil.execForStr("echo \"" + backupConfig.getCronExpr() + " " +
                        filename + "\" >> /var/spool/cron/root");*/
            } else {
                CronUtil.remove(code);
                CronUtil.schedule(code, backupConfig.getCronExpr(), () -> nonLinuxExecuteShell(code));
                // 支持秒级别定时任务
                //CronUtil.setMatchSecond(true);
                //CronUtil.start();
            }
            if (BackupConstant.BACKUP_CLOSE.equals(backupConfig.getEnable())) {
                backupConfigMapper.modifyBackupConfigEnable(code, BackupConstant.BACKUP_OPEN);
            }
        }
        return new Result<>(exec);
    }

    @Override
    public void onlyBuildCron(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        if (null != backupConfig){
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                String filename = getShellFilename(code);
                File file = new File(filename);
                buildShellFile(code, file);
                RuntimeUtil.execForStr("chmod 744 " + filename);
                if (BackupConstant.BACKUP_OPEN.equals(backupConfig.getEnable())) {
                    try {
                        RuntimeUtil.execForStr("sed -i /" + code + ".sh/d /var/spool/cron/root");
                    } catch (IORuntimeException e) {
                        e.printStackTrace();
                    }

                    File cronRootFile = new File("/var/spool/cron/root");
                    FileAppender appender = new FileAppender(cronRootFile, 16, true);
                    appender.append(backupConfig.getCronExpr() + " " + filename);
                    appender.flush();
                }
            } else {
                if (BackupConstant.BACKUP_OPEN.equals(backupConfig.getEnable())) {
                    CronUtil.remove(code);
                    CronUtil.schedule(code, backupConfig.getCronExpr(), () -> nonLinuxExecuteShell(code));
                }
            }
        }
    }

    @Override
    public Result<String> removeCron(String code) {
        BackupConfig backupConfig = backupConfigMapper.queryByCode(code);
        String resultMsg = code;
        if (null != backupConfig) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                /*throw new BackupException(100004, "配置类型不正确，不能删除Linux系统定时任务", code);*/
                //String filename = getShellFilename(code);
                try {
                    resultMsg = RuntimeUtil.execForStr("sed -i /" + code + ".sh/d /var/spool/cron/root");
                } catch (IORuntimeException e) {
                    e.printStackTrace();
                }
            } else {
                CronUtil.remove(code);
            }
            if (BackupConstant.BACKUP_OPEN.equals(backupConfig.getEnable())) {
                backupConfigMapper.modifyBackupConfigEnable(code, BackupConstant.BACKUP_CLOSE);
            }
        }
        return new Result<>(resultMsg);
    }

    @Override
    public void removeCronAndDeleteFile(BackupConfig backupConfig) {
        String code = backupConfig.getCode();
        if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
            try {
                RuntimeUtil.execForStr("sed -i /" + code + ".sh/d /var/spool/cron/root");
            } catch (IORuntimeException e) {
                e.printStackTrace();
            }
            String filename = getShellFilename(code);
            FileUtil.del(filename);
        } else {
            CronUtil.remove(code);
        }
    }

    /**
     *
     * Describe: 项目初始化时加载所有的开启的配置
     *
     * @param
     * @exception
     * @auther: luyangqian
     * @date: 2020-07-20 18:04
     */
    @Override
    public void initEnableCron() {
        List<BackupConfig> backupConfigs = backupConfigMapper.queryEnable(BackupConstant.BACKUP_OPEN);
        if (!CollectionUtils.isEmpty(backupConfigs)) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                if (!CollectionUtils.isEmpty(backupConfigs)) {
                    File file = new File("/var/spool/cron/root");
                    FileAppender appender = new FileAppender(file, 16, true);
                    for (BackupConfig backupConfig : backupConfigs) {
                        String filename = getShellFilename(backupConfig.getCode());
                        try {
                            RuntimeUtil.execForStr("sed -i /" + backupConfig.getCode() + ".sh/d /var/spool/cron/root");
                        } catch (IORuntimeException e) {
                            e.printStackTrace();
                        }
                        appender.append(backupConfig.getCronExpr() + " " + filename);
                    }
                    appender.flush();
                }
            } else {
                for (BackupConfig backupConfig : backupConfigs) {
                    String code = backupConfig.getCode();
                    CronUtil.schedule(code, backupConfig.getCronExpr(), () -> executeShell(code));
                }
                // 支持秒级别定时任务
                //CronUtil.setMatchSecond(true);
                CronUtil.start();
            }
        }
    }

    /**
     * 获取服务器上指定文件夹下的文件
     * @param recoverConfig
     * @return
     */
    @Override
    public Result<List<String>> queryDirFile(RecoverConfig recoverConfig) {
        String recoverDir = recoverConfig.getRecoverDir();
        if (StringUtils.isEmpty(recoverDir)) {
            throw new BackupException(100003, "恢复的路径配置不能为空", Collections.emptyList());
        }
        /*String cacheId = IdUtil.simpleUUID();
        ServerConfig serverConfig = new ServerConfig();
        BeanUtils.copyProperties(recoverConfig, serverConfig);*/
        //recoverConfigTimedCache.put(cacheId, serverConfig);

        if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(recoverConfig.getConnectType())) {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                return new Result<>(RuntimeUtil.execForLines("ls " + recoverDir));
            } else {
                return new Result<>(RuntimeUtil.execForLines("dir " + recoverDir));
            }
        } else {
            String serverCode = recoverConfig.getServerCode();
            String address = recoverConfig.getAddress();
            String password = recoverConfig.getPassword();
            String username = recoverConfig.getUsername();
            String port = recoverConfig.getPort();

            if (!StringUtils.isEmpty(serverCode)) {
                BackupServer backupServer = backupServerService.queryByRowId(serverCode);
                if (null != backupServer) {
                    //BeanUtils.copyProperties(backupServer, serverConfig);
                    //recoverConfigTimedCache.put(cacheId, serverConfig);
                    if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(backupServer.getConnectType())) {
                        return new Result<>(RuntimeUtil.execForLines("ls " + recoverDir));
                    } else {
                        address = backupServer.getAddress();
                        password = backupServer.getPassword();
                        username = backupServer.getUsername();
                        port = backupServer.getPort();
                    }
                }
            }
            /*if (cronType.equals(BackupConstant.DEFAULT_CORN_EXPR_TYPE)) {
                RuntimeUtil.execForStr("cd " + baseShellPath, "mkdir ")
            } else {

            }*/
            return new Result<>(RemoteRuntimeUtil
                    .execForLines(address, Integer.valueOf(port), username, password, "ls " + recoverDir));
        }
    }

    private ServerConfig getServerConfig(RecoverConfig recoverConfig) {
        if (null == recoverConfig) {
            throw new BackupException(100006, "服务器配置不能为空", "");
        }
        ServerConfig serverConfig = new ServerConfig();
        BeanUtils.copyProperties(recoverConfig, serverConfig);
        String serverCode = recoverConfig.getServerCode();
        if (!StringUtils.isEmpty(serverCode)) {
            BackupServer backupServer = backupServerService.queryByRowId(serverCode);
            if (null != backupServer) {
                BeanUtils.copyProperties(backupServer, serverConfig);
            }
        }
        return serverConfig;
    }

    @Override
    public InputStream downFile(RecoverConfig recoverConfig, String filename) {

        ServerConfig serverConfig = getServerConfig(recoverConfig);

        String recoverDir = serverConfig.getRecoverDir();
        /*if (!recoverDir.endsWith(File.separator)) {
            recoverDir = recoverDir + File.separator;
        }*/

        if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(serverConfig.getConnectType())) {
            if (!recoverDir.endsWith(File.separator)) {
                recoverDir = recoverDir + File.separator;
            }
            FileReader fileReader = new FileReader(recoverDir + filename);
            return fileReader.getInputStream();
        } else {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                if (!recoverDir.endsWith(File.separator)) {
                    recoverDir = recoverDir + File.separator;
                }
                try {
                    //RuntimeUtil.execForStr("cd " + baseShellPath);
                    RuntimeUtil.execForStr("mkdir " + baseShellPath + "/recoverTemporary");
                    RuntimeUtil.execForStr("sshpass -p " + serverConfig.getPassword() +
                            " scp -o StrictHostKeyChecking=no -P " + serverConfig.getPort() +
                            " -r " + serverConfig.getUsername() + "@" + serverConfig.getAddress() +
                            ":" + recoverDir + filename + " " + baseShellPath + "/recoverTemporary");
                    FileReader fileReader = new FileReader(baseShellPath + "/recoverTemporary/" + filename);
                    return fileReader.getInputStream();
                } finally {
                    //执行结束删掉中间的目录
                    RuntimeUtil.execForStr("rm -rf " + baseShellPath + "/recoverTemporary");
                }
            } else {
                if (!recoverDir.endsWith("/")) {
                    recoverDir = recoverDir + "/";
                }
                String recoverTemporaryPath = baseShellPath + File.separator + "recoverTemporary";
                try {
                    FileUtil.mkdir(recoverTemporaryPath);
                    if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(serverConfig.getConnectType())) {

                        //登录（帐号密码的SSH服务器）
                        Sftp sftp = new Sftp(serverConfig.getAddress(), Integer.valueOf(serverConfig.getPort()),
                                serverConfig.getUsername(), serverConfig.getPassword());

                        //下载远程文件
                        sftp.download(recoverDir + filename, FileUtil.file(recoverTemporaryPath));
                        FileReader fileReader = new FileReader(recoverTemporaryPath + File.separator + filename);
                        return fileReader.getInputStream();
                    } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(serverConfig.getConnectType())) {
                        //登录（帐号密码的FTP服务器）
                        Ftp ftp = new Ftp(serverConfig.getAddress(), Integer.valueOf(serverConfig.getPort()),
                                serverConfig.getUsername(), serverConfig.getPassword());
                        //下载远程文件
                        ftp.download(recoverDir + filename, FileUtil.file(recoverTemporaryPath));
                        FileReader fileReader = new FileReader(recoverTemporaryPath + File.separator + filename);
                        return fileReader.getInputStream();
                    }
                } finally {
                    FileUtil.del(recoverTemporaryPath);
                }
            }
        }
        return null;
    }

    @Override
    public Result<String> uploadFile(String filename, RecoverConfigVo recoverConfigVo) {

        ServerConfig targetServerConfig = getServerConfig(recoverConfigVo.getTargetRecover());
        String targetRecoverDir = targetServerConfig.getRecoverDir();
        if (StringUtils.isEmpty(targetRecoverDir)) {
            throw new BackupException(100005, "上传的路径配置不能为空", targetRecoverDir);
        }

        /*if (!recoverConfigTimedCache.containsKey(cacheId)) {
            throw new BackupException(100004, "操作间隔时间超过5分钟，已经失效", null);
        }*/

        /*ServerConfig serverConfig = recoverConfigTimedCache.get(cacheId);*/
        ServerConfig sourceServerConfig = getServerConfig(recoverConfigVo.getSourceRecover());
        /*if (null == serverConfig) {
            throw new BackupException(100004, "操作间隔时间超过5分钟，已经失效", null);
        }*/

        String sourceRecoverDir = sourceServerConfig.getRecoverDir();

        String recoverTemporaryPath = baseShellPath + File.separator + "recoverTemporary";
        FileUtil.mkdir(recoverTemporaryPath);

        try {
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                if (!sourceRecoverDir.endsWith(File.separator)) {
                    sourceRecoverDir = sourceRecoverDir + File.separator;
                }
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(sourceServerConfig.getConnectType())) {
                    FileUtil.copy(sourceRecoverDir + filename, recoverTemporaryPath, true);
                } else {
                    RuntimeUtil.execForStr("sshpass -p " + sourceServerConfig.getPassword() +
                            " scp -o StrictHostKeyChecking=no -P " + sourceServerConfig.getPort() +
                            " " + sourceServerConfig.getUsername() + "@" + sourceServerConfig.getAddress() +
                            ":" + sourceRecoverDir + filename + " " + recoverTemporaryPath);
                }
            } else {
                if (!sourceRecoverDir.endsWith("/")) {
                    sourceRecoverDir = sourceRecoverDir + "/";
                }
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(sourceServerConfig.getConnectType())) {
                    FileUtil.copy(sourceRecoverDir + filename, recoverTemporaryPath, true);
                } else if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(sourceServerConfig.getConnectType())) {
                    //登录（帐号密码的SSH服务器）
                    Sftp sftp = new Sftp(sourceServerConfig.getAddress(), Integer.valueOf(sourceServerConfig.getPort()),
                            sourceServerConfig.getUsername(), sourceServerConfig.getPassword());
                    //下载远程文件
                    sftp.download(sourceRecoverDir + filename, FileUtil.file(recoverTemporaryPath));
                } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(sourceServerConfig.getConnectType())) {
                    //登录（帐号密码的FTP服务器）
                    Ftp ftp = new Ftp(sourceServerConfig.getAddress(), Integer.valueOf(sourceServerConfig.getPort()),
                            sourceServerConfig.getUsername(), sourceServerConfig.getPassword());
                    //下载远程文件
                    ftp.download(sourceRecoverDir + filename, FileUtil.file(recoverTemporaryPath));
                }
            }

            String targetConnectType = targetServerConfig.getConnectType();
            String password = targetServerConfig.getPassword();
            String address = targetServerConfig.getAddress();
            String username = targetServerConfig.getUsername();
            String port = targetServerConfig.getPort();
            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(targetConnectType)) {
                    FileUtil.copy(recoverTemporaryPath + File.separator + filename, targetRecoverDir, true);
                } else {
                    RuntimeUtil.execForStr("sshpass -p " + password + " scp -o StrictHostKeyChecking=no -P " + port +
                            " " + recoverTemporaryPath + File.separator + filename + " " +
                            username + "@" + address +
                            ":" + targetRecoverDir);
                }
            } else {
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(targetConnectType)) {
                    FileUtil.copy(recoverTemporaryPath + File.separator + filename, targetRecoverDir, true);
                } else if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(targetConnectType)) {
                    //登录（帐号密码的SSH服务器）
                    Sftp sftp = new Sftp(address, Integer.valueOf(port),
                            username, password);
                    //上传本地文件
                    sftp.upload(targetRecoverDir, FileUtil.file(recoverTemporaryPath + File.separator + filename));
                } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(targetConnectType)) {
                    //登录（帐号密码的FTP服务器）
                    Ftp ftp = new Ftp(address, Integer.valueOf(port),
                            username, password);
                    //上传本地文件
                    ftp.upload(targetRecoverDir, FileUtil.file(recoverTemporaryPath + File.separator + filename));
                }
            }

        } finally {
            FileUtil.del(recoverTemporaryPath);
        }


        return new Result<>("");
    }

    @Override
    public Result<String> uploadFile(MultipartFile file, RecoverConfig recoverConfig) {

        ServerConfig serverConfig = getServerConfig(recoverConfig);

        String targetRecoverDir = serverConfig.getRecoverDir();

        if (StringUtils.isEmpty(targetRecoverDir)) {
            throw new BackupException(100005, "上传的路径配置不能为空", targetRecoverDir);
        }

        try (InputStream inputStream = file.getInputStream()){
            String name = file.getOriginalFilename();

            if (BackupConstant.DEFAULT_CORN_EXPR_TYPE.equals(cronType)) {
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(serverConfig.getConnectType())) {
                    if (!targetRecoverDir.endsWith(File.separator)) {
                        targetRecoverDir = targetRecoverDir + File.separator;
                    }
                    FileUtil.writeFromStream(inputStream, targetRecoverDir + name);
                } else {
                    String recoverTemporaryPath = baseShellPath + File.separator + "recoverTemporary";
                    FileUtil.mkdir(recoverTemporaryPath);
                    FileUtil.writeFromStream(inputStream, recoverTemporaryPath + File.separator + name);
                    try {
                        RuntimeUtil.execForStr("sshpass -p " + serverConfig.getPassword() +
                                " scp -o StrictHostKeyChecking=no -P " + serverConfig.getPort() +
                                " " + recoverTemporaryPath + File.separator + name + " " +
                                serverConfig.getUsername() + "@" + serverConfig.getAddress() +
                                ":" + targetRecoverDir);
                    } finally {
                        FileUtil.del(recoverTemporaryPath);
                    }
                }
            } else {
                if (BackupConstant.CONNECT_LOCAL_TYPE_VAR.equals(serverConfig.getConnectType())) {
                    if (!targetRecoverDir.endsWith(File.separator)) {
                        targetRecoverDir = targetRecoverDir + File.separator;
                    }
                    FileUtil.writeFromStream(inputStream, targetRecoverDir + name);
                } else {
                    String recoverTemporaryPath = baseShellPath + File.separator + "recoverTemporary";
                    FileUtil.mkdir(recoverTemporaryPath);
                    FileUtil.writeFromStream(inputStream, recoverTemporaryPath + File.separator + name);
                    try {
                        if (BackupConstant.DEFAULT_SSH_TYPE_VAR.equals(serverConfig.getConnectType())) {
                            //登录（帐号密码的SSH服务器）
                            Sftp sftp = new Sftp(serverConfig.getAddress(), Integer.valueOf(serverConfig.getPort()),
                                    serverConfig.getUsername(), serverConfig.getPassword());
                            //上传本地文件
                            sftp.upload(targetRecoverDir, FileUtil.file(recoverTemporaryPath + File.separator + name));
                        } else if (BackupConstant.CONNECT_FTP_TYPE_VAR.equals(serverConfig.getConnectType())) {
                            //登录（帐号密码的FTP服务器）
                            Ftp ftp = new Ftp(serverConfig.getAddress(), Integer.valueOf(serverConfig.getPort()),
                                    serverConfig.getUsername(), serverConfig.getPassword());
                            //上传本地文件
                            ftp.upload(targetRecoverDir, FileUtil.file(recoverTemporaryPath + File.separator + name));
                        }
                    } finally {
                        FileUtil.del(recoverTemporaryPath);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result<>("");
    }

    private Map<String, Object> packageReturn(String cacheId, List<String> executeReturn) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("cacheId", cacheId);
        map.put("rows", executeReturn);
        return map;
    }


}
