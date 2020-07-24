package com.leateck.gmp.backup.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: RecoverConfigVo</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-24   luyangqian  Created
 * </pre>
 */
@Data
public class RecoverConfigVo implements Serializable {

    private static final long serialVersionUID = 5997611678620891227L;

    private RecoverConfig sourceRecover;

    private RecoverConfig targetRecover;

}
