package com.template.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.model.event.ProductTookEvent;
import com.template.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventKafkaListener {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @KafkaListener(topics = "${spring.kafka.services.order.command.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> message) {
        log.info("MESSAGE RECEIVED: %s".formatted(message));
        var header = message.headers().lastHeader("command");
        if (header != null) {
            if (String.valueOf(header.value()).equals("ProductTookEvent")) {
                try {
                    paymentService.createPayment(objectMapper.readValue(message.value(), ProductTookEvent.class));
                } catch (Exception e) {
                    log.error("Ошибка десериализации ProductTookEvent");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
