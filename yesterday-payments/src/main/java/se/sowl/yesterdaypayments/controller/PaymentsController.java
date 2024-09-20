package se.sowl.yesterdaypayments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdaydomain.payment.domain.PaymentLog;
import se.sowl.yesterdaypayments.dto.PaymentCallbackRequest;
import se.sowl.yesterdaypayments.dto.PaymentConfirmRequest;
import se.sowl.yesterdaypayments.dto.PaymentRequest;
import se.sowl.yesterdaypayments.dto.PaymentResponse;
import se.sowl.yesterdaypayments.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentsController {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        log.info("Initiating payment: {}", request);
        PaymentLog paymentLog = paymentService.initiatePayment(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(new PaymentResponse(paymentLog.getOrderId()));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestBody PaymentConfirmRequest request) {
        log.info("Confirming payment: {}", request);
        paymentService.processPaymentCallback(request.getOrderId(), request.getPaymentKey(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
