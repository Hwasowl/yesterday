package se.sowl.yesterdayapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan(basePackages = {"se.sowl.yesterdaydomain"})
@EnableCaching
public class YesterdayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YesterdayApiApplication.class, args);
    }

}
