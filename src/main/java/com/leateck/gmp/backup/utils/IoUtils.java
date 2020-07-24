package com.leateck.gmp.backup.utils;

import cn.hutool.core.io.IoUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * <p>Title: IoUtils</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-22   luyangqian  Created
 * </pre>
 */
public class IoUtils {

    public static void exportStream(HttpServletResponse response, String name, InputStream inputStream) {
        try (OutputStream outputStream = response.getOutputStream()){
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;filename="+
                    URLEncoder.encode(name, "UTF-8"));
            IoUtil.copy(inputStream, outputStream, IoUtil.DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Pattern DEL_FILE_NAME_PATTERN = Pattern.compile("(gmp-backup)[0-9]{14}(.tar.gz|.zip)");
    /**
     *
     * @param ntime
     * @param file
     * @param saveDaySeconds
     */
    public static void deleteOverTimeFile(Long ntime, File file, long saveDaySeconds) {
        long ftime = file.lastModified();
        long ms = ntime - ftime;
           /*
            * 若是文件则直接删除
            * 若是目录，要先将目录多有内容删除
            * */
        if (file.isDirectory()) {
            //先清空目录
            File[] subs = file.listFiles();
            for (File sub : subs) {
                /*
                 * 递归调用   方法内部调用自己方法的行为  称为递归
                 * 使用要注意：
                 *   1.递归的层数不要太多，因为递归调用开销较大。
                 *   2.递归调用在方法中不能必然执行，必须在一个分支
                 *   中控制它的调用，否则就是死循环。  递归是将一个方法中所有的代码重新执行。
                 */
                deleteOverTimeFile(ntime, sub, saveDaySeconds);
            }
        }
        //和要保存的时间戳进行比较
        if (ms > saveDaySeconds) {
            if (DEL_FILE_NAME_PATTERN.matcher(file.getName()).matches()) {
                //递归删除文件方法
                file.delete();
            }
        }
    }

    public static void deleteOverTimeFile(Long ntime, File file, int saveDays) {
        deleteOverTimeFile(ntime, file, convertDaysToMilliseconds(saveDays));
    }

    /**
     * 天与毫秒的转换
     *
     * @param days
     * @return
     */
    public static long convertDaysToMilliseconds(int days) {
        return days * 24L * 3600 * 1000;
    }
}
