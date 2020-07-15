package com.leateck.gmp.backup.core.entity;

import com.leateck.gmp.backup.annoation.InsertField;
import com.leateck.gmp.backup.annoation.UpdateField;
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

    @InsertField
    private String rowId;

    @InsertField
    private String createDate;

    @UpdateField
    private String updateDate;
}
