package se.sowl.yesterdayapi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.yesterdaydomain.user.domain.CustomOAuth2User;
import se.sowl.yesterdaydomain.user.domain.User;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> getMe(@AuthenticationPrincipal Object principal) {
        if (principal instanceof CustomOAuth2User customOAuth2User) {
            User user = customOAuth2User.getUser();
            return ResponseEntity.ok(user.getEmail());
        } else if (principal instanceof UserDetails userDetails) {
            return ResponseEntity.ok(userDetails.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
