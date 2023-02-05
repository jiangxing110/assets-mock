package com.qbit.assets.common.utils;


import org.springframework.http.HttpStatus;

import java.util.HashMap;


/**
 * @author litao
 */
public class R extends HashMap<String, Object> {

    public R() {
        put("code", 200);
        put("message", "success");
    }

    public static R error() {
        return error("未知异常，请联系管理员", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static R error(String msg) {
        return error(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static R error(String msg, HttpStatus status) {
        R r = new R();
        r.put("code", status.value());
        r.put("message", msg);
        return r;
    }

    public static R ok(String data, String msg) {
        R r = new R();
        r.put("data", data);
        r.put("msg", msg);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(Object data) {
        R r = new R();
        r.put("data", data);
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
