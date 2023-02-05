package com.qbit.assets.common.annotation.currentUser;


import com.alibaba.druid.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author martinjiang
 * @version 1.0
 * @className MyLocaleResolver
 * @description 请描述类的业务用途
 * @date 2022/5/19 11:54
 **/
public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 从 request 域中读取传过来的参数
        // 声明 Locale 为默认语言显示
        Locale locale = Locale.getDefault();
        // 判断传入参数是否为空
        String l = request.getHeader("lang");
        if (!StringUtils.isEmpty(l)) {
            // 将传过来的参数，通过下划线分割，获取到地区(zh)即代码(CN)
            String[] split = l.split("_");
            // 进行赋值
            locale = new Locale(split[0], split[1]);
        }
        // 返回
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}