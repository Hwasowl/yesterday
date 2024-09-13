package se.sowl.yestdayrabbitmq.news;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewsRabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    public NewsRabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNewsUpdateMessage() {
        String message = "UPDATE_NEWS";
        String routingKey = "news.update";
        String exchangeName = "news-exchange";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
