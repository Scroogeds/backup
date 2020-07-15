package com.leateck.gmp.backup.annoation;

import java.lang.annotation.*;

/**
 * <p>Title: UpdateField</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface UpdateField {

    String value() default "";

}
