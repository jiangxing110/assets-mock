package com.qbit.assets.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Formatter;

/**
 * @author litao
 */
public class HexUtil {
    /**
     * 字节码转16进制
     *
     * @param bytes 字节码
     * @return 16进制
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        //完成16进制编码
        return sb.toString();
    }

    /**
     * 16进制转字节码
     *
     * @param hex 16进制
     * @return 字节码
     */
    public static byte[] hexToBytes(String hex) {
        byte[] digest = new byte[hex.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = hex.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     * uuid转16进制
     *
     * @param uuid uuid
     * @return 16进制
     */
    public static String UUIDToHex(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return uuid;
        }
        return uuid.replaceAll("-", "");
    }

    /**
     * 16进制转uuid
     *
     * @param hex 16进制
     * @return uuid
     */
    public static String hexToUUID(String hex) {
        return hex.replaceAll("([\\da-z]{8})([\\da-z]{4})([\\da-z]{4})([\\da-z]{4})([\\da-z]{12})", "$1-$2-$3-$4-$5");
    }
}
