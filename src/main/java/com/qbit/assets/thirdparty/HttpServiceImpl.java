package com.qbit.assets.thirdparty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.error.QbitError;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author liang
 */
@Service
@Slf4j
public class HttpServiceImpl implements HttpService {

    private ObjectMapper objectMapper;

    private OkHttpClient okHttpClient;


    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * 上传请求异常到aliyun sls
     *
     * @param request request {@link Request}
     */


    private Response requestResponse(Request request) {
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("okhttp3 put error >> ex = {}", QbitError.getStackTrace(e));
        }
        return null;
    }

    private String handleResponse(Response response) {
        if (response == null) {
            return "";
        }
        ResponseBody body = response.body();
        if (body == null) {
            response.close();
            return "";
        }
        try (response) {
            String res = body.string();
            if (!response.isSuccessful()) {
                HttpStatus status = HttpStatus.resolve(response.code());
                String reasonPhrase = status != null ? status.getReasonPhrase() : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
                throw new CustomException(StringUtils.isBlank(res) ? reasonPhrase : res);
            }
            return res;
        } catch (IOException e) {
            log.error("okhttp3 put error >> ex = {}", QbitError.getStackTrace(e));
        }
        return "";
    }

    public StringBuffer getQueryString(String url, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb;
    }

    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && headers.keySet().size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
    }

    @Override
    public String get(String url, Map<String, String> queries, Map<String, String> headers) {
        StringBuffer sb = getQueryString(url, queries);
        return getStr(sb.toString(), headers);
    }

    @Override
    public String getStr(String url, Map<String, String> headers) {
        Response response = getByStr(url, headers);
        return handleResponse(response);
    }

    @Override
    public Response getByStr(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        addHeaders(builder, headers);
        Request request = builder
                .url(url)
                .build();
        return requestResponse(request);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers) {
        String jsonString = "";
        if (params != null) {
            try {
                jsonString = objectMapper.writeValueAsString(params);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return postStr(url, jsonString, headers);
    }

    @Override
    public String postStr(String url, String params, Map<String, String> headers) {
        Response response = postByStr(url, params, headers);
        return handleResponse(response);
    }

    @Override
    public Response postByStr(String url, String params, Map<String, String> headers) {
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, params);
        Request.Builder builder = new Request.Builder();
        addHeaders(builder, headers);
        Request request = builder
                .url(url)
                .post(body)
                .build();
        return requestResponse(request);
    }
}
