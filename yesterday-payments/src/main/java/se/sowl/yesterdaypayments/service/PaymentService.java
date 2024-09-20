package se.sowl.yesterdaypayments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.sowl.yesterdaydomain.payment.domain.PaymentLog;
import se.sowl.yesterdaydomain.payment.domain.PaymentStatus;
import se.sowl.yesterdaydomain.payment.repository.PaymentLogRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentLogRepository paymentLogRepository;
//    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    @Value("${toss.payments.secret-key}")
    private String TOSS_SECRET_KEY;
    private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments/confirm";

    public PaymentLog initiatePayment(String userId, BigDecimal amount) {
        String orderId = generateOrderId();
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setUserId(userId);
        paymentLog.setAmount(amount);
        paymentLog.setStatus(PaymentStatus.PENDING);
        paymentLog.setOrderId(orderId);
        paymentLog.setCreatedAt(LocalDateTime.now());
        return paymentLogRepository.save(paymentLog);
    }

    public void processPaymentCallback(String orderId, String paymentKey, BigDecimal amount) {
        PaymentLog paymentLog = paymentLogRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));

            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("orderId", orderId);
            paymentData.put("amount", amount);
            paymentData.put("paymentKey", paymentKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(paymentData, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(TOSS_API_URL, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                paymentLog.setStatus(PaymentStatus.SUCCESS);
                paymentLogRepository.save(paymentLog);

            // TODO: HTTP Rabitmq 호출
    //                rabbitTemplate.convertAndSend("payment-exchange", "payment.success",
    //                    new PaymentSuccessEvent(paymentLog.getUserId(), paymentLog.getOrderId()));
            } else {
                paymentLog.setStatus(PaymentStatus.FAILED);
                paymentLogRepository.save(paymentLog);
            }
        } catch (HttpClientErrorException e) {
            paymentLog.setStatus(PaymentStatus.FAILED);
            paymentLogRepository.save(paymentLog);
            throw new RuntimeException("Payment confirmation failed: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            paymentLog.setStatus(PaymentStatus.FAILED);
            paymentLogRepository.save(paymentLog);
            throw new RuntimeException("Payment confirmation failed", e);
        }
    }

    private String generateOrderId() {
        return "ORDER_" + System.currentTimeMillis();
    }
}
