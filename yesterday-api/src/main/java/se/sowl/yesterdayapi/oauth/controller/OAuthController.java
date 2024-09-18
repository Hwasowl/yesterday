package se.sowl.yesterdayapi.oauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdayapi.common.CommonResponse;
import se.sowl.yesterdayapi.common.config.security.jwt.JwtTokenProvider;
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
        boolean b = jwtTokenProvider.validateToken(token);
        System.out.println(b + " : " + token);
        return ResponseEntity.ok(b);
    }
}
