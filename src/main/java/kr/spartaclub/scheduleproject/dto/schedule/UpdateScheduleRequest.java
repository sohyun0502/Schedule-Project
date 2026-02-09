package kr.spartaclub.scheduleproject.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateScheduleRequest {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 20, message = "제목은 20자 이하로 입력하세요.")
    private String title;
    private String content;
}
