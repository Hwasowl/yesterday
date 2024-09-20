package se.sowl.yesterdayauth.common.config.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import se.sowl.yesterdayauth.common.config.security.jwt.JwtTokenProvider;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${spring.front.url}")
    private String frontUrl;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String jwt = jwtTokenProvider.createToken(authentication);
        response.addHeader("Authorization", "Bearer " + jwt);

        String targetUrl = UriComponentsBuilder.fromUriString(frontUrl)
            .queryParam("token", jwt)
            .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
