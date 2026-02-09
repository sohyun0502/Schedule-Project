package kr.spartaclub.scheduleproject.service;

import jakarta.validation.Valid;
import kr.spartaclub.scheduleproject.config.PasswordEncoder;
import kr.spartaclub.scheduleproject.dto.user.*;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encodedPassword
        );
        User savedUser = userRepository.save(user);
        return new SignupResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public SessionUser login(@Valid LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );
        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return new SessionUser(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );
        return new GetUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetUserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(
                user -> new GetUserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getModifiedAt()
                )
        ).toList();
    }

    @Transactional
    public UpdateUserResponse updateUser(Long id, UpdateUserRequest request) {
        // 본인 제외 이메일 중복 체크
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        user.update(request.getName(), request.getEmail(), encodedPassword);
        return new UpdateUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    @Transactional
    public void deleteUser(Long id) {
        boolean existence = userRepository.existsById(id);
        if (!existence) {
            throw new IllegalStateException("없는 유저입니다.");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );
    }

}
