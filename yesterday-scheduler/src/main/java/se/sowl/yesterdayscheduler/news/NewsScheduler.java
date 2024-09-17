package se.sowl.yesterdayscheduler.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsScheduler {
    private final RestTemplate restTemplate;
    private static final String RABBITMQ_SERVICE_URL = "http://localhost:9000/api/message";

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
