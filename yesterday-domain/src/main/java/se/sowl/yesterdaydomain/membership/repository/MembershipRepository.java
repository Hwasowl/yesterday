package se.sowl.yesterdaydomain.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.yesterdaydomain.membership.domain.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByUserId(Long userId);
}
