package kr.spartaclub.scheduleproject.service;

import jakarta.validation.Valid;
import kr.spartaclub.scheduleproject.dto.user.*;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        User savedUser = userRepository.save(user);
        return new SignupResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public SessionUser login(@Valid LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );
        // 비밀번호 검증
        if (!ObjectUtils.nullSafeEquals(user.getPassword(), request.getPassword())) {
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
                user.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public List<GetUserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(
                user -> new GetUserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )
        ).toList();
    }

    @Transactional
    public UpdateUserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );
        user.update(request.getName(),request.getEmail(),request.getPassword());
        return new UpdateUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
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
}
