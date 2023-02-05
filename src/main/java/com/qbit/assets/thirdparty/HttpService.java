package com.qbit.assets.thirdparty;

import okhttp3.Response;

import java.util.Map;

/**
 * @author litao
 */
public interface HttpService {
    /**
     * get请求
     *
     * @param url     url
     * @param queries queries
     * @param headers headers
     * @return response text
     */
    String get(String url, Map<String, String> queries, Map<String, String> headers);

    /**
     * get请求
     *
     * @param url     url
     * @param headers headers
     * @return response text
     */
    String getStr(String url, Map<String, String> headers);

    /**
     * get请求(返回Response对象)
     *
     * @param url     url
     * @param headers header
     * @return Response
     */
    Response getByStr(String url, Map<String, String> headers);

    /**
     * post请求
     *
     * @param url     url
     * @param params  params
     * @param headers headers
     * @return response text
     */
    String post(String url, Map<String, Object> params, Map<String, String> headers);

    /**
     * post请求
     *
     * @param url     url
     * @param params  params
     * @param headers headers
     * @return response text
     */
    String postStr(String url, String params, Map<String, String> headers);

    /**
     * post请求(返回Response对象)
     *
     * @param url     url
     * @param headers header
     * @return Response
     */
    Response postByStr(String url, String params, Map<String, String> headers);
}
