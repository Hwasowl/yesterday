package se.sowl.yesterdayauth.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "se.sowl.yesterdaydomain")
public class JpaConfig {
}
