package com.melon.portfoliomanager.configs.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configuration = new HashMap<>() {{
            put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        }};
        return new KafkaAdmin(configuration);
    }

    @Bean
    public Map<String, NewTopic> topics() {
        return new HashMap<>() {{
            put("stock-prices-changes", new NewTopic("stock-prices-changes", 1, (short) 1));
        }};
    }
}
