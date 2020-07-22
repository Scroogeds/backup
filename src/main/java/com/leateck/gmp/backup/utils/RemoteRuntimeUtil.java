package com.leateck.gmp.backup.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: RemoteRuntimeUtil</p>
 * <p>Description: 远程服务器执行linux命令</p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-21   luyangqian  Created
 * </pre>
 */
public class RemoteRuntimeUtil {

    /**
     * 获得shell执行结果，会话已经连接的
     *
     * @param session 会话
     * @return 执行结果
     */
    public static List<String> execForLines(Session session, String... cmds) throws IORuntimeException {
        BufferedReader reader = null;
        Channel channel = null;
        List<String> executeLs = new ArrayList<>();
        for (String cmd : cmds) {
            try {
                //session.connect();
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(cmd);

                channel.connect();
                InputStream inputStream = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String buf = null;
                while (null != (buf = reader.readLine())) {
                    executeLs.add(buf);
                }

            } catch (JSchException | IOException e) {
                e.printStackTrace();
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != channel) {
                    channel.disconnect();
                }
                //session.disconnect();
            }
        }
        return executeLs;
    }

    /**
     * 获得shell执行结果
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名
     * @param sshPass 密码
     * @return 执行结果
     */
    public static List<String> execForLines(String sshHost, int sshPort, String sshUser, String sshPass, String... cmds) throws IORuntimeException {
        Session session = JschUtil.getSession(sshHost, sshPort, sshUser, sshPass);
        return execForLines(session, cmds);
    }


}
