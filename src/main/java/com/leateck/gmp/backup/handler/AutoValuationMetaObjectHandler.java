package com.leateck.gmp.backup.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.leateck.gmp.backup.annoation.InsertField;
import com.leateck.gmp.backup.annoation.UpdateField;
import com.leateck.gmp.backup.constant.BackupConstant;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * <p>Title: AutoValuationMetaObjectHandler</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-15   luyangqian  Created
 * </pre>
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })})
public class AutoValuationMetaObjectHandler implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //sql 类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            //获取参数
            Object parameter = ((Map)invocation.getArgs()[1]).get("param1");
            //获取私有成员变量

            if (parameter instanceof Map) {
                //支持批量batchInsert插入字段
                Set<Map.Entry<String, Object>> entrySet = ((Map<String, Object>) parameter).entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        List tempList = (List) value;
                        for (Object obj : tempList) {
                            Field[] declaredFields = ReflectUtil.getFields(obj.getClass());
                            fillFiled(declaredFields, sqlCommandType, obj);
                        }
                    }
                }
            } else {
                Field[] declaredFields = ReflectUtil.getFields(parameter.getClass());
                fillFiled(declaredFields, sqlCommandType, parameter);
            }
        }

        return invocation.proceed();
    }

    private void fillFiled(Field[] declaredFields, SqlCommandType sqlCommandType, Object obj) throws Throwable {
        for (Field field : declaredFields) {
            //field.setAccessible(true);

            //insert 语句插入InsertField
            if (SqlCommandType.INSERT.equals(sqlCommandType) && null != field.getAnnotation(InsertField.class)) {
                field.setAccessible(true);
                if (BackupConstant.BASE_FIELD_ROW_ID.equals(field.getName())) {
                    field.set(obj, IdUtil.simpleUUID());
                } else if (BackupConstant.BASE_FIELD_CREATE_TIME.equals(field.getName())) {
                    field.set(obj, DateUtil.now());
                }
            }
            if (SqlCommandType.UPDATE.equals(sqlCommandType) && null != field.getAnnotation(UpdateField.class)) {
                field.setAccessible(true);
                if (BackupConstant.BASE_FIELD_UPDATE_TIME.equals(field.getName())) {
                    field.set(obj, DateUtil.now());
                }
            }

            //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
            /*if (field.get(obj) == null) {
                field.set(obj, "");
            }*/
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
