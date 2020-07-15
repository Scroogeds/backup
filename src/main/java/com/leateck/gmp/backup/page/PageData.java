package com.leateck.gmp.backup.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: PageData</p>
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
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1085456151311571775L;

    private int pageNum;

    private int pageSize;

    private long total;

    private int pages;

    private List<T> rows;

}
