package se.sowl.yesterdaynews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"se.sowl.yesterdaydomain", "se.sowl.yesterdaynews"})
@EntityScan(basePackages = {"se.sowl.yesterdaydomain"})
@EnableJpaRepositories(basePackages = {"se.sowl.yesterdaydomain"})
public class YesterdayNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayNewsApplication.class, args);
    }

}
