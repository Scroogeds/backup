package com.leateck.gmp.backup.base.mapper;

import java.util.List;

/**
 * <p>Title: BaseMapper</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-16   luyangqian  Created
 * </pre>
 */
public interface BaseMapper<T> {

    List<T> queryByPage();

}
