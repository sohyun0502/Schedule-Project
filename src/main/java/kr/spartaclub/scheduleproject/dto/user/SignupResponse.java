package kr.spartaclub.scheduleproject.dto.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public SignupResponse(Long id, String name, String email, String password, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
