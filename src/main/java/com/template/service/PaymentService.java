package com.template.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.mapper.PaymentMapper;
import com.template.model.event.PaymentCanceledEvent;
import com.template.model.event.ProductTookEvent;
import com.template.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String COMMAND_HEADER_NAME = "command";

    @Value("${spring.kafka.services.order.command.topic}")
    private String orderCommandTopic;

    @Value("${payment.mode}")
    private Boolean paymentMode;

    private final ObjectMapper objectMapper;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void createPayment(ProductTookEvent event) {
        var paymentEntity = paymentMapper.toPaymentEntity(event);
        paymentRepository.save(paymentEntity);

        try {
            if (paymentMode) {
                kafkaTemplate.send(
                        MessageBuilder.withPayload(objectMapper.writeValueAsString(paymentMapper.toPaymentDoneEvent(paymentEntity)))
                                .setHeader(KafkaHeaders.TOPIC, orderCommandTopic)
                                .setHeader(COMMAND_HEADER_NAME, "PaymentDoneEvent")
                                .build()
                );
            } else {
                var paymentCancelledEvent = new PaymentCanceledEvent();
                paymentCancelledEvent.setName(event.getName());
                kafkaTemplate.send(
                        MessageBuilder.withPayload(objectMapper.writeValueAsString(paymentCancelledEvent))
                                .setHeader(KafkaHeaders.TOPIC, orderCommandTopic)
                                .setHeader(COMMAND_HEADER_NAME, "PaymentCancelledEvent")
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Ошибка при сериализации PaymentDoneEvent");
            throw new RuntimeException(e);
        }
    }
}
