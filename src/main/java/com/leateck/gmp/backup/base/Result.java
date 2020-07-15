package com.leateck.gmp.backup.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title: Result</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-14   luyangqian  Created
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public Result(T data) {
        this.data = data;
        this.code = 0;
        this.msg = "操作成功";
    }
}
