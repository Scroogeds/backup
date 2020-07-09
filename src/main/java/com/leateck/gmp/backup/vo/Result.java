package com.leateck.gmp.backup.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: Result</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
@Data
public class Result implements Serializable {

    private String result;

    private Object data;

}
