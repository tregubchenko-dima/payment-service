package com.template.model.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDoneEvent {

    private String orderName;
    private Integer price;
    private String status;
    private LocalDate date;
}
