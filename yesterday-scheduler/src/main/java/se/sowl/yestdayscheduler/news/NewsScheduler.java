package se.sowl.yestdayscheduler.news;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.sowl.yestdayscheduler.rabbitmq.RabbitMQService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NewsScheduler {
    private final RabbitMQService rabbitMQService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduleNewsUpdate() {
        rabbitMQService.sendMessage("news-update-queue", "UPDATE_NEWS");
        System.out.println("Scheduled news update message sent at " + LocalDateTime.now());
    }
}
