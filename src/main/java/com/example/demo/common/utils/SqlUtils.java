package com.example.demo.common.utils;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

public class SqlUtils {
    private static final int HASHMAP_SIZE = 63;
    /**
     * 获取aop中的SQL语句
     *
     * @param pjp
     * @param sqlSessionFactory
     * @return
     * @throws IllegalAccessException
     */
    public static String getMybatisSql(ProceedingJoinPoint pjp, SqlSessionFactory sqlSessionFactory) throws Exception {
        Map<String, Object> map = new HashMap<>(HASHMAP_SIZE);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String namespace = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Configuration configuration = sqlSessionFactory.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement(namespace + "." + methodName);
        Object[] objects = pjp.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object object = objects[i];
            if (parameterAnnotations[i].length == 0) {
                if (object != null){

                    if (object.getClass().getClassLoader() == null && object instanceof Map) {
                        map.putAll((Map<? extends String, ?>) object);
                    } else {
                        map.putAll(objectToMap(object));
                    }
                }
            } else {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof Param) {
                        map.put(((Param) annotation).value(), object);
                    }
                }
            }
        }
        BoundSql boundSql = mappedStatement.getBoundSql(map);
        return showSql(configuration, boundSql);
    }

    /**
     * 解析BoundSql，生成不含占位符的SQL语句
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                String value = Matcher.quoteReplacement(getParameterValue(parameterObject));
                sql = sql.replaceFirst("\\?", value);
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();

                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        String value = Matcher.quoteReplacement(getParameterValue(obj));
                        sql = sql.replaceFirst("\\?", value);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        String value = Matcher.quoteReplacement(getParameterValue(obj));
                        sql = sql.replaceFirst("\\?", value);
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 若为字符串或者日期类型，则在参数两边添加''
     *
     * @param obj
     * @return
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(HASHMAP_SIZE);
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            if ("size".equals(fieldName)) {
                value = castList(obj, Object.class);
                map.put("idList", value);
            } else {
                value = field.get(obj);
                map.put(fieldName, value);
            }
        }
        return map;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}