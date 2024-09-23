package se.sowl.yesterdayrabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendRegisterMembershipMessage(String message) {
        String routingKey = "membership.register";
        String exchangeName = "membership-exchange";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}

