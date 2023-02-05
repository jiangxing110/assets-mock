package com.qbit.assets.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author litao
 */
public class ShaUtil {

    public static String encrypt(String message, String secret) {
        return encrypt(message, secret, "HmacSHA512");
    }

    public static String encrypt(String message, String secret, String algorithm) {
        Key sk = new SecretKeySpec(secret.getBytes(), algorithm);
        Mac mac;
        try {
            mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] bytes = mac.doFinal(message.getBytes());
        return HexUtil.bytesToHex(bytes);
    }

    public static boolean verify(String encryptText, String message, String secret, String algorithm) {
        String text = encrypt(message, secret, algorithm);
        return StringUtils.equalsIgnoreCase(encryptText, text);
    }
}
