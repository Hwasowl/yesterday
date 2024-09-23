package se.sowl.yesterdaymembership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.yesterdaydomain.membership.domain.Membership;
import se.sowl.yesterdaydomain.membership.repository.MembershipRepository;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public void registerMembership(Long userId, Long orderId) {
        Membership membership = new Membership(userId, orderId);
        membershipRepository.save(membership);
    }

    public boolean isMember(Long userId) {
        return membershipRepository.existsByUserId(userId);
    }
}

