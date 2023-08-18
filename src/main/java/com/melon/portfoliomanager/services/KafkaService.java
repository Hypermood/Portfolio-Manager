package com.melon.portfoliomanager.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.portfoliomanager.utils.SerializationUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("kafka")
public class KafkaService implements MessageBrokerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NewTopic topic;
    private final SerializationUtils serializationUtils;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, NewTopic topic,
                        SerializationUtils serializationUtils) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.serializationUtils = serializationUtils;
    }

    public void sendMessage(Map<String, ?> message) throws JsonProcessingException {
        kafkaTemplate.send(topic.name(), serializationUtils.serializeMapToJsonString(message));
    }
}
