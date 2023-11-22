package com.github.spark.lib.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T clone(T object, Class<T> clazz) {
        try {
            return mapper.readValue(mapper.writeValueAsString(object), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Cloning via Jackson failed", e);
        }
    }
}
