package com.leateck.gmp.backup.base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leateck.gmp.backup.base.mapper.BaseMapper;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.page.PageData;
import com.leateck.gmp.backup.page.SearchParamWrapper;
import com.leateck.gmp.backup.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: BaseService</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
public abstract class BaseService<M extends BaseMapper<T>, T> {

    private M baseMapper;

    @Autowired
    public void setBaseMapper(M baseMapper) {
        this.baseMapper = baseMapper;
    }

    protected PageData<T> pageSearch(SearchParamWrapper paramWrapper) {
        int pageNum = paramWrapper.getPageNum();
        int pageSize = paramWrapper.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<T> datas = baseMapper.queryByPage();
        return PageUtil.getPageData(new PageInfo<>(datas));
    }
}
