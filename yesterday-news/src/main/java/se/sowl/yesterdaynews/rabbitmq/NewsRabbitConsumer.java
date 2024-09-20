package se.sowl.yesterdaynews.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import se.sowl.yesterdaynews.service.news.NewsService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsRabbitConsumer {
    private final NewsService newsService;

    @RabbitListener(queues = "news-update-queue")
    public void receiveNewsUpdateMessage(String message) {
        try {
            if ("UPDATE_NEWS".equals(message)) {
                newsService.saveYesterdayNews();
                log.info("메세지 처리 완료: UPDATE_NEWS");
            } else {
                log.warn("해석할 수 없는 메세지를 받았어요.: {}", message);
            }
        } catch (Exception e) {
            log.error("뉴스 갱신 메세지 큐 처리 도중 문제가 생겼어요.", e);
            // TODO: retry queue?
        }
    }
}
