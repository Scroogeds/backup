package com.leateck.gmp.backup.utils;

import com.github.pagehelper.PageInfo;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;

/**
 * <p>Title: PageUtil</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
public class PageUtil {

    public static <T> PageData<T> getPageData(PageInfo<T> pageInfo) {
        PageData<T> pageData = new PageData<>();
        pageData.setPageNum(pageInfo.getPageNum());
        pageData.setPageSize(pageInfo.getPageSize());
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPages(pageInfo.getPages());
        pageData.setRows(pageInfo.getList());
        return pageData;
    }
}
