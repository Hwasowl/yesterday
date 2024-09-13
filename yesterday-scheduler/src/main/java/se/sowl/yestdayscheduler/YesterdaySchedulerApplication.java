package se.sowl.yestdayscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YesterdaySchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(YesterdaySchedulerApplication.class, args);
    }
}
