package aston.homework.kafka;

import aston.homework.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topic.user-events}")
    private String topic;

    public void sendUserEvent(UserEvent event) {
        log.info("Отправка события в Kafka: {}", event);
        kafkaTemplate.send(topic, event.getEmail(), event);
    }
}
