package se.sowl.yestdaykafka.kafka.news;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NewsKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNewsUpdate(String newsId) {
        kafkaTemplate.send("news-update-topic", newsId);
    }

    public void sendNewsForSummarization(String newsContent) {
        kafkaTemplate.send("news-summarization-topic", newsContent);
    }
}
