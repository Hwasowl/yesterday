package se.sowl.yesterdayscheduler.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {
    private final RestTemplate restTemplate;
    @Value("${spring.rabbitmq.service.url}")
    private String RABBITMQ_SERVICE_URL;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduleNewsUpdate() {
        try {
            String message = "UPDATE_NEWS";
            restTemplate.postForObject(RABBITMQ_SERVICE_URL, message, String.class);
            log.info("뉴스 갱신 메세지 발행 일자: {}", java.time.LocalDateTime.now());
        } catch (Exception e) {
            log.error("뉴스 갱신 메세지 발행 실패: ", e);
        }
    }
}
