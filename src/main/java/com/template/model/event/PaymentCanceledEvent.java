package com.template.model.event;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentCanceledEvent {

    private UUID orderId;
    private String name;
}
