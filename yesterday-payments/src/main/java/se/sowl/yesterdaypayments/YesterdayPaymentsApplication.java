package se.sowl.yesterdaypayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "se.sowl.yesterdaydomain.payment.repository")
@EntityScan(basePackages = "se.sowl.yesterdaydomain.payment.domain")
public class YesterdayPaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayPaymentsApplication.class, args);
    }
}
