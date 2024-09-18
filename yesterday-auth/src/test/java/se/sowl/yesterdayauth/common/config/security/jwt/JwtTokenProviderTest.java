package se.sowl.yesterdayauth.common.config.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "testSecretKeytestSecretKeytestSecretKeytestSecretKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        // Given
        User user = new User("testUser", "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // When
        String token = jwtTokenProvider.createToken(auth);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("토큰에서 인증 정보를 추출할 수 있어야 한다.")
    void getAuthentication() {
        // Given
        User user = new User("testUser", "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String token = jwtTokenProvider.createToken(auth);

        // When
        Authentication resultAuth = jwtTokenProvider.getAuthentication(token);

        // Then
        assertNotNull(resultAuth);
        assertEquals("testUser", resultAuth.getName());
        assertTrue(resultAuth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("토큰에서 인증 정보를 추출할 수 없는 경우 IllegalArgumentException을 던져야 한다.")
    void validateToken() {
        // Given
        User user = new User("testUser", "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String token = jwtTokenProvider.createToken(auth);

        // When & Then
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("유효하지 않은 토큰인 경우 false를 반환해야 한다.")
    void validateTokenInvalid() {
        // Given
        String invalidToken = "invalidToken";

        // When & Then
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}
