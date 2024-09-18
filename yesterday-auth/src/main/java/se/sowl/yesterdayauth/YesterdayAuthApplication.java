package se.sowl.yesterdayauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan(basePackages = {"se.sowl.yesterdaydomain"})
@EnableCaching
public class YesterdayAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayAuthApplication.class, args);
    }

}
