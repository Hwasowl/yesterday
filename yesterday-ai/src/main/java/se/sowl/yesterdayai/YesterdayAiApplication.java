package se.sowl.yesterdayai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"se.sowl.yesterdaydomain", "se.sowl.yesterdayai"}, exclude = { SecurityAutoConfiguration.class })
@EntityScan(basePackages = {"se.sowl.yesterdaydomain"})
public class YesterdayAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayAiApplication.class, args);
    }

}
