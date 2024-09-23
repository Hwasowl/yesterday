package se.sowl.yesterdayrabbitmq.config;

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
        return new Queue("news-update-queue", true);
    }

    @Bean
    public Queue membershipQueue() {
        return new Queue("membership-register-queue", true);
    }

    @Bean
    public TopicExchange newsExchange() {
        return new TopicExchange("news-exchange");
    }

    @Bean
    public TopicExchange membershipExchange() {
        return new TopicExchange("membership-exchange");
    }

    @Bean
    public Binding newsBinding(Queue newsQueue, TopicExchange newsExchange) {
        return BindingBuilder.bind(newsQueue).to(newsExchange).with("news.update");
    }

    @Bean
    public Binding membershipBinding(Queue membershipQueue, TopicExchange membershipExchange) {
        return BindingBuilder.bind(membershipQueue).to(membershipExchange).with("membership.register");
    }
}
