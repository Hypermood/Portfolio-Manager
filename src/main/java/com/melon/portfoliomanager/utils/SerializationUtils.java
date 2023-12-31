package com.melon.portfoliomanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SerializationUtils {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serializeMapToJsonString(Map<String, ?> mapToBeSerialized) throws JsonProcessingException {
        return objectMapper.writeValueAsString(mapToBeSerialized);
    }
}
