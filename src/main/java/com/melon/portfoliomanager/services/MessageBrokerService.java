package com.melon.portfoliomanager.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface MessageBrokerService {
    void sendMessage(Map<String, ?> message) throws JsonProcessingException;
}
