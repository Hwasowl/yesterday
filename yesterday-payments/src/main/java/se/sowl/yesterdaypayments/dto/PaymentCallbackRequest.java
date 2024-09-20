package se.sowl.yesterdaypayments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentCallbackRequest {
    private String orderId;
    private boolean success;
}
