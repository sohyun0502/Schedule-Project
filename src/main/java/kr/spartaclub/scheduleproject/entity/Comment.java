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
    private String name;
    private String password;

    // 외래키 생성 = FOREIGN KEY (schedule_id) REFERENCES schedule(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(Schedule schedule, String content, String name, String password) {
        this.schedule = schedule;
        this.content = content;
        this.name = name;
        this.password = password;
    }
}
