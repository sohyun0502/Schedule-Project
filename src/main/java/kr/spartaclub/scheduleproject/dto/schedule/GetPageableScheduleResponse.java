package kr.spartaclub.scheduleproject.dto.schedule;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetPageableScheduleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final int commentCount;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetPageableScheduleResponse(Long id, String title, String content, int commentCount, String name, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.name = name;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
