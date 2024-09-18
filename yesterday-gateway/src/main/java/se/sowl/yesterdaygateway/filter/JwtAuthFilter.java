package se.sowl.yesterdaygateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey key;

    public JwtAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            System.out.println(token);
            if (token != null && token.startsWith("Bearer ")) {
                return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/api/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(isValid -> {
                        if (isValid) {
                            return chain.filter(exchange);
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    });
            }

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static class Config {
        // 필요한 경우 여기에 설정을 추가할 수 있습니다.
    }
}
