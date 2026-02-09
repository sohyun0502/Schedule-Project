package kr.spartaclub.scheduleproject.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.spartaclub.scheduleproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotBlank String email);

    boolean existsByEmail(@NotBlank(message = "이메일은 필수 입력값입니다.") @Email(message = "이메일 형식이 아닙니다.") String email);
}
