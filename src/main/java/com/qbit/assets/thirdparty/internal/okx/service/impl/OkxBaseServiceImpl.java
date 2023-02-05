package com.qbit.assets.thirdparty.internal.okx.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.*;
import com.qbit.assets.thirdparty.HttpService;
import com.qbit.assets.thirdparty.ThirdPartyProperties;
import com.qbit.assets.thirdparty.configuration.OkxConfiguration;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.Res;
import com.qbit.assets.thirdparty.internal.okx.annotation.ITag;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.BaseDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * okx请求基类封装
 *
 * @author litao
 */
@Slf4j
public class OkxBaseServiceImpl {
    @Resource
    private ThirdPartyProperties properties;

    @Resource
    private HttpService httpService;

    public static <T> T getListFirst(List<T> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return data.get(0);
    }


    protected OkxConfiguration getConfiguration() {
        OkxConfiguration configuration = properties.getOkx();
        if (configuration == null) {
            throw new CustomException("okx配置文件未找到");
        }
        return configuration;
    }

    protected String getHost() {
        return getConfiguration().getHost();
    }

    /**
     * 获取数据签名
     *
     * @param timestamp 时间戳
     * @param method    请求方法
     * @param path      request path
     * @param body      request body
     * @return sign
     */
    private String getSign(String timestamp, HttpMethod method, String path, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp)
                .append(method.name())
                .append(path);
        if (method == HttpMethod.POST && StringUtils.isNotBlank(body)) {
            sb.append(body);
        }
        String sign = ShaUtil.encrypt(sb.toString(), getConfiguration().getSecret(), "HmacSHA256");
        byte[] bytes = HexUtil.hexToBytes(sign);
        sign = Base64.getEncoder().encodeToString(bytes);
        return sign;
    }

    /**
     * 获取通用请求头
     *
     * @param method method
     * @param path   request path
     * @param params request body
     * @return headers
     */
    protected Map<String, String> getHeaders(HttpMethod method, String path, String params) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = formatter.format(new Date());

        HashMap<String, String> headers = new HashMap<>(5);
        headers.put("OK-ACCESS-KEY", getConfiguration().getKey());
        headers.put("OK-ACCESS-SIGN", getSign(timestamp, method, path, params));
        headers.put("OK-ACCESS-TIMESTAMP", timestamp);
        headers.put("OK-ACCESS-PASSPHRASE", getConfiguration().getPassphrase());
        if (!SpringContextUtil.isProd()) {
            headers.put("x-simulated-trading", "1");
        }
        return headers;
    }

    protected String handleResponse(Response response) {
        if (response == null) {
            return "";
        }
        ResponseBody body = response.body();
        if (body == null) {
            response.close();
            return "";
        }
        try (response) {
            return body.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String get(String url, Map<String, Object> params) {
        String path = url;
        if (params != null && !params.isEmpty()) {
            path = url + "?" + QueryString.stringify(params);
        }
        log.info(path);
        Response response = httpService.getByStr(getHost() + path, getHeaders(HttpMethod.GET, path, null));

        return handleResponse(response);
    }


    protected String post(String url, BaseDTO body) {
        System.out.println(body);
        String params = JsonUtil.toJSONString(body);
        //.replaceAll("\\s", "");
        Response response = httpService.postByStr(getHost() + url, params, getHeaders(HttpMethod.POST, url, params));
        return handleResponse(response);
    }

    protected <T> T getData(Res<T> value) {
        if (value == null) {
            return null;
        }
        if (StringUtils.isNotBlank(value.getMessage())) {
            throw new CustomException(value.getMessage(), value.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return value.getData();
    }

    /**
     * 读取通用返回值中的数据
     *
     * @param response  response
     * @param valueType valueType
     * @return T
     */
    private <T> T readValue(String response, JavaType valueType) {
        log.info("okx response: {}", response);
        return getData(JsonUtil.toBean(response, valueType));
    }

    /**
     * 数据类型转换(okx data会使用一层list包装, 默认返回list 0)
     *
     * @param response       response
     * @param parameterTypes parameterTypes
     * @return T
     */
    protected <T> T convert(String response, JavaType parameterTypes) {
        List<T> data = convertList(response, parameterTypes);
        return getListFirst(data);
    }

    /**
     * 数据类型转换(okx data会使用一层list包装, 默认返回list 0)
     *
     * @param response response
     * @param clazz    clazz
     * @return T
     */
    protected <T> T convert(String response, Class<T> clazz) {
        List<T> data = convertList(response, clazz);
        return getListFirst(data);
    }

    /**
     * 数据类型转换
     *
     * @param response response
     * @param clazz    clazz
     * @return T
     */
    protected <T> List<T> convertList(String response, Class<T> clazz) {
        JavaType valueType = JsonUtil.getCollectionType(List.class, clazz);
        return readValue(response, JsonUtil.getCollectionType(Res.class, valueType));
    }

    /**
     * 数据类型转换
     *
     * @param response       response
     * @param parameterTypes parameterTypes
     * @return T
     */
    protected <T> List<T> convertList(String response, JavaType parameterTypes) {
        JavaType valueType = JsonUtil.getCollectionType(List.class, parameterTypes);
        return readValue(response, JsonUtil.getCollectionType(Res.class, valueType));
    }

    protected void setBrokerCode(ITag entity) {
        String brokerCode = getConfiguration().getBrokerCode();
        if (StringUtils.isNotBlank(brokerCode)) {
            entity.setTag(brokerCode);
        }
    }
}
