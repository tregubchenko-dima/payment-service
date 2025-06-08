package com.template.repository;


import com.template.model.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с {@link PaymentEntity}
 */
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
