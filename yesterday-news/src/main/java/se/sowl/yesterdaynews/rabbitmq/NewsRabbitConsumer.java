package se.sowl.yesterdaynews.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import se.sowl.yesterdaynews.service.NewsService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsRabbitConsumer {
    private final NewsService newsService;

    @RabbitListener(queues = "news-update-queue")
    public void receiveNewsUpdateMessage(String message) {
        if ("UPDATE_NEWS".equals(message)) {
            newsService.saveYesterdayNews();
        } else {
            log.error("Unknown queues message: {}", message);
        }
    }
}
