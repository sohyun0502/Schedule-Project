package kr.spartaclub.scheduleproject.repository;

import jakarta.validation.constraints.NotBlank;
import kr.spartaclub.scheduleproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotBlank String email);
}
