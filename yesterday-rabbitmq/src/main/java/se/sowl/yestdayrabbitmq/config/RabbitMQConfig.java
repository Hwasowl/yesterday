package se.sowl.yestdayrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue newsQueue() {
        return new Queue("news-queue", true);
    }

    @Bean
    public TopicExchange newsExchange() {
        return new TopicExchange("news-exchange");
    }

    @Bean
    public Binding binding(Queue newsQueue, TopicExchange newsExchange) {
        return BindingBuilder.bind(newsQueue).to(newsExchange).with("news.create");
    }
}