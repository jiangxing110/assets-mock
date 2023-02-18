package com.qbit.assets.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @author LiTao litaoh@aliyun.com
 */
public class ValidatorUtil {
    @NotNull
    private static final Pattern UUID = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern EMAIL = Pattern.compile("^([A-Za-z0-9_\\-.])+@([A-Za-z0-9_\\-.])+\\.([A-Za-z]{2,9})$");
    private static final Pattern HASH_32 = Pattern.compile("^[0-9a-fA-F]{32}$");

    private static final Pattern CHINESE = Pattern.compile("[\\u4e00-\\u9fa5]");

    private static boolean validator(Pattern reg, String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return reg.matcher(value).matches();
    }

    /**
     * 验证uuid格式
     *
     * @param uuid uuid
     * @return boolean
     */
    public static boolean isUUID(String uuid) {
        return validator(UUID, uuid);
    }

    /**
     * 验证邮箱格式
     *
     * @param email email
     * @return boolean
     */
    public static boolean isEmail(String email) {
        return validator(EMAIL, email);
    }

    /**
     * 验证hash长度是否为32位
     *
     * @param hash hash
     * @return boolean
     */
    public static boolean isHash32(String hash) {
        return validator(HASH_32, hash);
    }

    /**
     * 检测是否存在中文
     *
     * @param content content
     * @return boolean
     */
    public static boolean isChinese(String content) {
        return validator(CHINESE, content);
    }
}
