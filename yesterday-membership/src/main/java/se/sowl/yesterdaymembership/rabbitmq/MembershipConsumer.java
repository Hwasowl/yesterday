package se.sowl.yesterdaymembership.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaymembership.service.MembershipService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipConsumer {
    private final MembershipService membershipService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "membership-register-queue")
    public void receiveMembershipRegistration(String message) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(message);
        log.info("Received message: {}", jsonNode);
        Long orderId = jsonNode.get("orderId").asLong();
        Long userId = jsonNode.get("userId").asLong();
        membershipService.registerMembership(userId, orderId);
    }
}
