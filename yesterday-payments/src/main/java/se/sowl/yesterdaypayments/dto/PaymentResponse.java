package se.sowl.yesterdaypayments.dto;

import lombok.Getter;

@Getter
public class PaymentResponse {
    private String orderId;

    public PaymentResponse(String orderId) {
        this.orderId = orderId;
    }
}
