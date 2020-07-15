package com.leateck.gmp.backup.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.leateck.gmp.backup.core.entity.BackupConfig;
import com.leateck.gmp.backup.core.entity.BackupServer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;

/**
 * <p>Title: NullValueAspect</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
//@Aspect
//@Component
public class NullValueAspect {

    @Pointcut("execution(public * com.leateck.gmp.backup.core.mapper..*.insert*(..))")
    public void insert() { }

    @Pointcut("execution(public * com.leateck.gmp.backup.core.mapper..*.modify*(..))")
    public void modify() {}

    @Pointcut("insert() || modify()")
    public void pointcut() {}

    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        for (Object obj : args) {
            if (isBackupType(obj)) {
                Field[] declaredFields = ReflectUtil.getFields(obj.getClass());
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    if (field.get(obj) == null) {
                        field.set(obj, "");
                    }
                }
            }
        }
    }

    private boolean isBackupType(Object obj) {
        return obj instanceof BackupConfig || obj instanceof BackupServer;
    }

}
