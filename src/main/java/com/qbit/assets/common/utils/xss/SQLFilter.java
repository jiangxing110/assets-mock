package com.qbit.assets.common.utils.xss;


import com.qbit.assets.common.error.CustomException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author martinjiang
 */
public class SQLFilter {
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new CustomException("包含非法字符");
            }
        }

        return str;
    }
}
