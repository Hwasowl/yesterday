package se.sowl.yesterdaypayments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private String userId;
    private BigDecimal amount;
}
