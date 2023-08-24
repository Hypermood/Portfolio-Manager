package com.melon.portfoliomanager.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("kafka")
public class KafkaService implements MessageBrokerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, NewTopic> topics;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, Map<String, NewTopic> topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    public void sendMessage(String message) {
        String topicName = "stock-prices-changes";
        validateTopicName(topicName);
        kafkaTemplate.send(topicName, message);
    }

    private void validateTopicName(String topicName) {
        if (!topics.containsKey(topicName)) {
            logger.error(String.format("Error while getting topic from application context while trying to send " +
                    "message to Kafka ! No topic with name topic_name=%s exists in application context !", topicName));
            throw new RuntimeException(String.format("Error during sending message to Kafka ! No topic with name " +
                    "topic_name='%s' exists in application context !", topicName));
        }
    }
}
