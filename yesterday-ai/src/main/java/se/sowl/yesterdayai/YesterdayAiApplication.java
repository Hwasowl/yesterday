package se.sowl.yesterdayai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "se.sowl.yesterdaydomain.news.repository")
@EntityScan(basePackages = "se.sowl.yesterdaydomain.news.domain")
public class YesterdayAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayAiApplication.class, args);
    }
}
