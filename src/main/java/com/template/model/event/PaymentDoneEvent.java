package com.template.model.event;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PaymentDoneEvent {

    private UUID orderId;
    private String orderName;
    private Integer price;
    private String status;
    private LocalDate date;
}
