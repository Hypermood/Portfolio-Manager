package com.melon.portfoliomanager.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.portfoliomanager.utils.SerializationUtils;
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
    private final SerializationUtils serializationUtils;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, Map<String, NewTopic> topics,
                        SerializationUtils serializationUtils) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
        this.serializationUtils = serializationUtils;
    }

    public void sendMessage(Map<String, ?> message) throws JsonProcessingException {
        String topicName = "stock-prices-changes";
        if (!topics.containsKey(topicName)) {
            logger.error(String.format("Error while getting topic from application context while trying to send " +
                    "message to Kafka ! No topic with name topic_name=%s exists in application context !", topicName));
            throw new RuntimeException("Error during sending message to Kafka ! No such topic exists in application " +
                    "context !");
        }
        kafkaTemplate.send(topicName, serializationUtils.serializeMapToJsonString(message));
    }
}
