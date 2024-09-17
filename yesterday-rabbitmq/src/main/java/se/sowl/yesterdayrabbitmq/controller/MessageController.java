package se.sowl.yesterdayrabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdayrabbitmq.producer.NewsRabbitProducer;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final NewsRabbitProducer newsRabbitProducer;

    @PostMapping("/api/message")
    public void sendMessage(@RequestBody String message) {
        newsRabbitProducer.sendNewsUpdateMessage(message);
    }
}
