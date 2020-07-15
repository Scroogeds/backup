package com.leateck.gmp.backup.exception;

import lombok.*;

/**
 * <p>Title: BackupException</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BackupException extends RuntimeException {

    private int code;

    private String msg;

    private Object data;

}
