package com.featuredoc.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonWriter<T> {

    private final T object;

    public JsonWriter(T object) {
        this.object = object;
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this.object);
    }

}
