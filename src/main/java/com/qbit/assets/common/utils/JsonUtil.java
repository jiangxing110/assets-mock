package com.qbit.assets.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author martinjiang
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, JavaType... parameterTypes) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, parameterTypes);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toBean(Object obj, Class<T> clazz) {
        return toBean(toJSONString(obj), clazz);
    }

    public static <T> T toBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toBean(Object obj, JavaType javaType) {
        return toBean(toJSONString(obj), javaType);
    }

    public static <T> T toBean(String str, JavaType javaType) {
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> parse(String str) {
        try {
            return OBJECT_MAPPER.readValue(str, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJSONString(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断字符串是否为json
     *
     * @param inputJson
     * @return
     * @throws IOException
     */
    public static boolean isJsonValid(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(inputJson);
        JsonNode jsonObj = mapper.readTree(parser);
        System.out.println(jsonObj.toString());
        return true;
    }

}
