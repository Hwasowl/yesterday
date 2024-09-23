package se.sowl.yesterdaymembership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sowl.yesterdaymembership.service.MembershipService;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkMembership(@RequestParam Long userId) {
        boolean isMember = membershipService.isMember(userId);
        return ResponseEntity.ok(isMember);
    }
}
