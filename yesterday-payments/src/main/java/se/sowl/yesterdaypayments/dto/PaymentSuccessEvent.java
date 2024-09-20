package se.sowl.yesterdaypayments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentSuccessEvent {
    private String userId;
    private String orderId;
}
