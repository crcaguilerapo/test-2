package org.crcaguilerapo.paymentPlatform.infrastructure.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Serialization {
    private final ObjectMapper objectMapper;


    public Serialization(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> String objectToJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}