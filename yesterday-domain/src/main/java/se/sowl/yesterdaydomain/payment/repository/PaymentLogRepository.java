package se.sowl.yesterdaydomain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.yesterdaydomain.payment.domain.PaymentLog;

import java.util.Optional;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
    Optional<PaymentLog> findByOrderId(String orderId);
}
