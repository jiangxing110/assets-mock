package com.qbit.assets.common.utils;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qbit.assets.common.utils.xss.SQLFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class Query {
    /**
     * 获取实体类的表名
     *
     * @param clazz table entity
     * @return table name
     */
    public static String getTableName(Class<?> clazz) {
        TableInfo table = SqlHelper.table(clazz);
        if (table == null) {
            return null;
        }
        return table.getTableName();
    }

    /**
     * 实体类是否定义了字段
     *
     * @param entityClass entity
     * @param field       field
     * @return ret
     */
    private static <T> boolean isDeclaredField(Class<T> entityClass, String field) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        List<String> fields = Arrays.stream(declaredFields).map(Field::getName).toList();
        if (fields.size() == 0) {
            return false;
        }
        return fields.contains(field);
    }

    /**
     * 解析map数据为wrapper
     *
     * @param entityClass entity
     * @param params      map data
     * @return wrapper
     */
    public static <T> QueryWrapper<T> parseWrapper(Class<T> entityClass, Map<String, Object> params) {
        return parseWrapper(entityClass, params, null, true);
    }

    /**
     * 解析map数据为wrapper
     *
     * @param entityClass entity
     * @param params      map data
     * @param excludes    需要排除的字段
     * @param camel       是否开启驼峰命名
     * @return wrapper
     */
    public static <T> QueryWrapper<T> parseWrapper(Class<T> entityClass, Map<String, Object> params, String[] excludes, boolean camel) {
        return parseWrapper(new QueryWrapper<>(), entityClass, params, excludes, camel);
    }

    /**
     * 解析map数据为wrapper
     *
     * @param wrapper     wrapper
     * @param entityClass entity
     * @param params      map data
     * @param excludes    需要排除的字段
     * @param camel       是否开启驼峰命名
     * @return wrapper
     */
    public static <T, E extends AbstractWrapper<T, String, ? extends AbstractWrapper<T, String, ?>>> E parseWrapper(
            E wrapper,
            Class<T> entityClass,
            Map<String, Object> params,
            String[] excludes,
            boolean camel
    ) {
        if (params == null || params.isEmpty()) {
            return wrapper;
        }
        Field[] declaredFields = entityClass.getDeclaredFields();
        Class<?> superclass = entityClass.getSuperclass();
        Field[] superFields = superclass.getDeclaredFields();
        List<String> fields = Arrays.stream(ArrayUtils.addAll(declaredFields, superFields)).map(Field::getName).toList();
        if (fields.size() == 0) {
            return wrapper;
        }
        BiPredicate<String, Object> filter = (k, v) -> {
            if (excludes != null && Arrays.asList(excludes).contains(k)) {
                return false;
            }
            return "createTime".equals(k) || fields.contains(k);
        };
        params.forEach((k, v) -> {
            if (v instanceof String) {
                if (StringUtils.contains((String) v, ',')) {
                    v = ((String) v).split(",");
                }
            }
            if (filter.test(k, v) && v != null) {
                String column;
                if (camel) {
                    column = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(k);
                } else {
                    column = "\"" + k + "\"";
                }
                if (v instanceof Object[] section) {
                    if (column.toLowerCase().contains("time") || column.toLowerCase().contains("date")) {
                        // 时间相关的字段使用>= <=
                        if (section.length == 1) {
                            wrapper.ge(column, section[0]);
                        } else {
                            wrapper.ge(column, section[0]);
                            wrapper.le(column, section[1]);
                        }
                    } else {
                        if (section.length == 0) {
                            wrapper.eq("1", 0);
                        } else {
                            wrapper.in(column, section);
                        }
                    }
                } else if (v instanceof List list) {
                    if (list.size() == 0) {
                        wrapper.eq("1", 0);
                    } else {
                        wrapper.in(column, list);
                    }
                } else {
                    if (column.contains("time") || column.contains("date")) {
                        wrapper.ge(column, v);
                    } else {
                        wrapper.eq(column, v);
                    }
                }
            }
        });
        return wrapper;
    }

    public static <T> IPage<T> getPage(Class<T> entityClass, Map<String, Object> params) {
        return getPage(entityClass, params, null);
    }

    public static <T> IPage<T> getPage(Class<T> entityClass, Map<String, Object> params, String defaultOrderField) {
        return getPage(entityClass, params, defaultOrderField, false);
    }

    public static <T> IPage<T> getPage(Class<T> entityClass, Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 0;
        long limit = 10;

        if (params.get(Constant.PAGE) != null) {
            String val = params.get(Constant.PAGE).toString();
            curPage = Long.parseLong(val);
        }
        curPage += 1;
        if (params.get(Constant.LIMIT) != null) {
            String val = params.get(Constant.LIMIT).toString();
            limit = Long.parseLong(val);
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String) params.get(Constant.ORDER_FIELD));
        String order = (String) params.get(Constant.ORDER);


        //前端字段排序
        if (StringUtils.isNotEmpty(orderField) && isDeclaredField(entityClass, orderField)) {
            if (Constant.ASC.equalsIgnoreCase(order)) {
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if (StringUtils.isBlank(defaultOrderField)) {
            return page;
        }

        //默认排序
        if (isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        } else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
