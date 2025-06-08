package com.template.model.event;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductTookEvent {

    private UUID orderId;
    private String name;
    private Integer price;
    private Integer count;
}
