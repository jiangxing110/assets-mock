package com.qbit.assets.common.utils;

import java.util.Map;

/**
 * @author LiTao litaoh@aliyun.com
 */
public class QueryString {
    public static String stringify(Map<String, Object> queries) {
        StringBuilder sb = new StringBuilder();
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, Object> entry : queries.entrySet()) {
                if (firstFlag) {
                    firstFlag = false;
                } else {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}
