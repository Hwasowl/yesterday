package se.sowl.yesterdaynews.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NewsKafkaConsumer {

    @KafkaListener(topics = "news-topic", groupId = "news-group")
    public void listenNewsGroup(String message) {
        System.out.println("Received Message in group 'news-group': " + message);
        // TODO: Implement logic to process the received message
        // This could involve updating the news status, triggering additional processes, etc.
    }
}
