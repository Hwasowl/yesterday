package se.sowl.yesterdaymembership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "se.sowl.yesterdaydomain.news.repository")
@EntityScan(basePackages = "se.sowl.yesterdaydomain.news.domain")
public class YesterdayMembershipApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayMembershipApplication.class, args);
    }
}
