package com.leateck.gmp.backup.page;

import com.leateck.gmp.backup.constant.BackupConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: SearchParamWrapper</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Data
public class SearchParamWrapper implements Serializable {

    private static final long serialVersionUID = -5887609637706318175L;

    private String params;

    private String fulltext;

    private String[] sorts;

    private int pageNum = BackupConstant.SEARCH_DEFAULT_PAGE_NUM;

    private int pageSize = BackupConstant.SEARCH_DEFAULT_PAGE_SIZE;

}
