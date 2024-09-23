package se.sowl.yesterdayauth.oauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdayauth.common.CommonResponse;
import se.sowl.yesterdayauth.common.config.security.jwt.JwtTokenProvider;
import se.sowl.yesterdaydomain.user.domain.CustomOAuth2User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/session")
    public CommonResponse<Map<String, Object>> getSession(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            return CommonResponse.ok(Map.of("isLoggedIn", false));
        }
        Map<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("isLoggedIn", true);
        sessionInfo.put("user", user.getAttributes());
        return CommonResponse.ok(sessionInfo);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        boolean b = jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Long> getUserInfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(userId);
    }
}
