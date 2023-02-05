package com.qbit.assets.common.error;


import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <a href="https://zentao.qbitnetwork.com/zentao/task-view-1586.html">按需求分装QbitError</a>
 * 自动上传exception到aliyun
 *
 * @author EDZ
 * @description QbitError
 * @date 2022/11/17 14:37
 */
@Component
public class QbitError {


    /**
     * 根据异常获取异常堆栈
     *
     * @param throwable 异常
     * @return stack trace
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

  
}
