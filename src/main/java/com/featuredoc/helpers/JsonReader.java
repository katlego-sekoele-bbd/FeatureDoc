package com.featuredoc.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader<T> {

    private final String json;
    private final Class<T> tagetClass;

    public JsonReader(String json, Class<T> targetClass) {
        this.json = json;
        this.tagetClass = targetClass;
    }

    public T toObject() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.json, tagetClass);
    }

}
