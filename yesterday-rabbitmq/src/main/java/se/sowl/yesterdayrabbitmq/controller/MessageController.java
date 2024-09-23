package se.sowl.yesterdayrabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdayrabbitmq.producer.MembershipProducer;
import se.sowl.yesterdayrabbitmq.producer.NewsProducer;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final NewsProducer newsProducer;
    private final MembershipProducer membershipProducer;

    @PostMapping("/rabbitmq/news/update")
    public void sendNewsUpdateMessage(@RequestBody String message) {
        newsProducer.sendNewsUpdateMessage(message);
    }

    @PostMapping("/rabbitmq/payments/success")
    public void sendRegisterMembershipMessage(@RequestBody String message) {
        membershipProducer.sendRegisterMembershipMessage(message);
    }
}
