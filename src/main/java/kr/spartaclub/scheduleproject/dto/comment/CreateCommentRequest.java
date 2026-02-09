package kr.spartaclub.scheduleproject.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String content;
}
