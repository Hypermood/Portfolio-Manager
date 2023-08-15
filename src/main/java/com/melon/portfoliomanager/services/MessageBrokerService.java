package com.melon.portfoliomanager.services;

import java.util.Map;

public interface MessageBrokerService {
    void sendMessage(Map<String, ?> message);
}
