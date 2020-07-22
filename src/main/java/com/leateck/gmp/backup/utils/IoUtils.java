package com.leateck.gmp.backup.utils;

import cn.hutool.core.io.IoUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

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
}
