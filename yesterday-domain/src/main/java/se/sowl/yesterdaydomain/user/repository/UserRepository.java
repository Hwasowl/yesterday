package se.sowl.yesterdaydomain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.yesterdaydomain.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, String provider);
}
