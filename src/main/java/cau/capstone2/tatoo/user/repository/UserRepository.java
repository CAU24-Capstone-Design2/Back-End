package cau.capstone2.tatoo.user.repository;

import cau.capstone2.tatoo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    boolean existsById(Long userId);
    Optional<User> findByKakaoId(Long kakaoid);
}
