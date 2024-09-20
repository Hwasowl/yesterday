package se.sowl.yesterdaypayments.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentConfirmRequest {
    private String orderId;
    private String paymentKey;
    private BigDecimal amount;
}
