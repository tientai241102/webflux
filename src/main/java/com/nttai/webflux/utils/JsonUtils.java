package com.nttai.webflux.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * The Class JsonUtil.
 */
@Component
public class JsonUtils {

    private ObjectMapper objectMapper;
    private static JsonUtils instance;
    private static ObjectMapper staticObjectMapper;

    public static JsonUtils getInstance() {
        return instance;
    }

    public static String objectToJson(Object obj) {
        try {
            return staticObjectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            // log
        }
        return StringUtils.EMPTY;
    }

    /**
     * Instantiates a new json util.
     */

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        instance = this;
        staticObjectMapper = objectMapper;
    }

    /**
     * Gets the generic object.
     *
     * @param <T>   the generic type
     * @param input the input
     * @param clazz the clazz
     * @return the generic object
     */
    public <T> T getGenericObject(Object input, Class<T> clazz) {
        return objectMapper.convertValue(input, clazz);
    }

    public <T> T getEntityFromJsonStr(String input, Class<T> clazz) {
        try {
            return objectMapper.readValue(input, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the generic object.
     *
     * @param <T>   the generic type
     * @param input the input
     * @param clazz the clazz
     * @return the generic object
     */
    public <T> T getEntityFromJsonObj(Object input, Class<T> clazz) {
        return objectMapper.convertValue(input, clazz);
    }

    /**
     * Gets the generic object list.
     *
     * @param input   the input
     * @param typeRef the type ref
     * @return the generic object list
     */
    public <T> T getGenericObjectList(Object input, TypeReference<T> typeRef) {
        return objectMapper.convertValue(input, typeRef);
    }

    public <T> T getGenericStringList(String input, TypeReference<T> typeRef)
            throws IOException {
        return objectMapper.readValue(input, typeRef);
    }

    public String getValueFromJsonStr(String jsonStr, String key, boolean trimValue) {
        try {
            Map<String, String> map =
                    objectMapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {
                    });
            String val = map.get(key);
            if (val != null) {
                if (trimValue) {
                    val = val.trim();
                }
                return val;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public String printObjectToLog(Object obj) {
        try {
            return staticObjectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // log
        }
        return "";
    }

    public String toJson(Object obj) {
        try {
            return staticObjectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            // log
        }
        return StringUtils.EMPTY;
    }

    public <T> T convertInputStreamToClass(InputStream input, Class<T> clazz) {
        try {
            return objectMapper.readValue(input, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
