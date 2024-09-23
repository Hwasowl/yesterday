package se.sowl.yesterdaypayments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.sowl.yesterdaydomain.payment.domain.PaymentLog;
import se.sowl.yesterdaydomain.payment.domain.PaymentStatus;
import se.sowl.yesterdaydomain.payment.repository.PaymentLogRepository;
import se.sowl.yesterdaypayments.dto.PaymentSuccessEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentLogRepository paymentLogRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.rabbitmq.service.url}")
    private String RABBITMQ_SERVICE_URL;

    @Value("${toss.payments.secret-key}")
    private String TOSS_SECRET_KEY;
    private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments/confirm";

    public PaymentLog initiatePayment(String userId, BigDecimal amount) {
        String orderId = generateOrderId();
        PaymentLog paymentLog = generatePaymentLog(userId, amount, orderId);
        return paymentLogRepository.save(paymentLog);
    }

    public void processPaymentCallback(String orderId, String paymentKey, BigDecimal amount) {
        PaymentLog paymentLog = paymentLogRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
        try {
            HttpEntity<Map<String, Object>> request = generatePaymentRequest(orderId, paymentKey, amount);
            ResponseEntity<Map> response = restTemplate.postForEntity(TOSS_API_URL, request, Map.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                recordPaymentStatus(paymentLog, PaymentStatus.FAILED);
                return;
            }
            recordPaymentStatus(paymentLog, PaymentStatus.SUCCESS);
            restTemplate.postForObject(RABBITMQ_SERVICE_URL, generateSuccessEventMessage(paymentLog), String.class);
        } catch (Exception e) {
            recordPaymentStatus(paymentLog, PaymentStatus.FAILED);
            throw new RuntimeException("결제 요청에 실패했어요. 사유: ", e);
        }
    }

    private static PaymentSuccessEvent generateSuccessEventMessage(PaymentLog paymentLog) {
        return new PaymentSuccessEvent(paymentLog.getUserId(), paymentLog.getOrderId());
    }

    private void recordPaymentStatus(PaymentLog paymentLog, PaymentStatus failed) {
        paymentLog.setStatus(failed);
        paymentLogRepository.save(paymentLog);
    }

    private static PaymentLog generatePaymentLog(String userId, BigDecimal amount, String orderId) {
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setUserId(userId);
        paymentLog.setAmount(amount);
        paymentLog.setStatus(PaymentStatus.PENDING);
        paymentLog.setOrderId(orderId);
        paymentLog.setCreatedAt(LocalDateTime.now());
        return paymentLog;
    }

    private HttpEntity<Map<String, Object>> generatePaymentRequest(String orderId, String paymentKey, BigDecimal amount) {
        HttpHeaders headers = generatePaymentHeader();
        Map<String, Object> paymentBody = generatePaymentBody(orderId, paymentKey, amount);
        return new HttpEntity<>(paymentBody, headers);
    }

    private HttpHeaders generatePaymentHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));
        return headers;
    }

    private static Map<String, Object> generatePaymentBody(String orderId, String paymentKey, BigDecimal amount) {
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("orderId", orderId);
        paymentData.put("amount", amount);
        paymentData.put("paymentKey", paymentKey);
        return paymentData;
    }

    private String generateOrderId() {
        return "ORDER_" + System.currentTimeMillis();
    }
}
