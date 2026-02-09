package kr.spartaclub.scheduleproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // private Long scheduleId; -> 외래키 생성 안하고 이런식으로 연관관계 없이 개발 가능!
    // 만약 Schedule과 Comment 테이블이 물리적으로 분리되어 있으면 연관관계 맺기 불가능

    public Comment(Schedule schedule, String content, User user) {
        this.schedule = schedule;
        this.content = content;
        this.user = user;
    }
}
