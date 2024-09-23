package se.sowl.yesterdayrabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewsProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendNewsUpdateMessage(String message) {
        String routingKey = "news.update";
        String exchangeName = "news-exchange";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
