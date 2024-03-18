package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }
