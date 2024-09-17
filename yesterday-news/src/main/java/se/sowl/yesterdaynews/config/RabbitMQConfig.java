package se.sowl.yesterdaynews.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue newsQueue() {
        return new Queue("news-update-queue", true);
    }
}
