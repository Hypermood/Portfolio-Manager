package com.melon.portfoliomanager.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("kafka")
public class KafkaService implements MessageBrokerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NewTopic topic;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendMessage(Map<String, ?> companyStocksWithExtremePriceChanges) {
        kafkaTemplate.send(topic.name(), companyStocksWithExtremePriceChanges.toString());
    }
}
