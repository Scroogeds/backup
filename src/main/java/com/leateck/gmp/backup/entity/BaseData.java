package com.leateck.gmp.backup.entity;

import lombok.Data;

/**
 * <p>Title: BaseData</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-13   luyangqian  Created
 * </pre>
 */
@Data
public class BaseData {

    private String rowId;

    private String createDate;

    private String updateDate;
}
