package com.leateck.gmp.backup.handler;

import com.leateck.gmp.backup.base.entity.Result;
import com.leateck.gmp.backup.exception.BackupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: GlobalExceptionHandler</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public Result<Object> handleException(Exception e, HttpServletRequest request) throws Throwable {

        int code = 500;
        String msg = e.getMessage();
        Object data = "";

        logger.error("=========== GlobalExceptionHandler, URI: " + request.getRequestURI() + ", METHOD: " + request.getMethod());

        if (e instanceof BackupException) {
            BackupException backupException = (BackupException) e;
            code = backupException.getCode();
            msg = backupException.getMsg();
            data = backupException.getData();
            logger.error("=========== GlobalExceptionHandler: code: " + code + ",msg: " + msg);
            return new Result<>(code, msg, data);
        } else {
            logger.error("=========== GlobalExceptionHandler: ", e);
        }

        return new Result<>(code, msg, data);
    }
}
