package se.sowl.yestdaykafka.news;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsKafkaConsumer {

    @KafkaListener(topics = "news-update-topic", groupId = "news-group")
    public void listenNewsUpdate(String newsId) {
        // TODO: 뉴스 업데이트 컨트롤러 호출
    }

    @KafkaListener(topics = "news-summarization-topic", groupId = "news-group")
    public void listenNewsSummarization(String newsContent) {
        // TODO: 뉴스 요약 컨트롤러 구현
    }
}
