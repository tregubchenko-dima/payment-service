package com.template.mapper;

import com.template.model.entity.PaymentEntity;
import com.template.model.event.PaymentDoneEvent;
import com.template.model.event.ProductTookEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.UUID;


@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "orderName", source = "event.name")
    @Mapping(target = "price", source = "event.price")
    @Mapping(target = "status", constant = "PAYMENT_SUCCESS")
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    PaymentEntity toPaymentEntity(ProductTookEvent event);

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "orderName", source = "entity.orderName")
    @Mapping(target = "price", source = "entity.price")
    @Mapping(target = "status", source = "entity.status")
    @Mapping(target = "date", source = "entity.date")
    PaymentDoneEvent toPaymentDoneEvent(UUID orderId, PaymentEntity entity);
}
