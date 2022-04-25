package com.psyala.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, clazz);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(o);
    }
}
