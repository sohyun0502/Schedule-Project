package kr.spartaclub.scheduleproject.controller;

import jakarta.validation.Valid;
import kr.spartaclub.scheduleproject.dto.schedule.*;
import kr.spartaclub.scheduleproject.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(
            @RequestAttribute Long userId,
            @Valid @RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.saveSchedule(userId, request));
    }

    // 일정 조회 - 선택 일정 조회
    @GetMapping("/schedules/{id}")
    public ResponseEntity<GetOneScheduleResponse> getSchedule(
            @RequestAttribute Long userId,
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedule(userId, id));
    }

    // 일정 조회 - 전체 일정 조회
    @GetMapping("/schedules")
    public ResponseEntity<List<GetAllScheduleResponse>> getSchedules(
            @RequestAttribute Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedules(userId));
    }

    // 일정 수정
    @PutMapping("/schedules/{id}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @RequestAttribute Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(userId, id, request));
    }

    // 일정 삭제
    // 원래 @DeleteMapping은 Body가 없음. 하지만 요즘 최신 버전의 HTTP는 Body가 있어서 @RequestBody 사용가능
    // 이전에는 @DeleteMapping 대신 @PostMapping을 써서 @RequestBody 사용
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @RequestAttribute Long userId,
            @PathVariable Long id) {
        scheduleService.deleteSchedule(userId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 일정 페이징 조회
    @GetMapping("/schedules")
    public ResponseEntity<Page<GetPageableScheduleResponse>> getPageableSchedules(
            @RequestAttribute Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getPageableSchedules(userId, page, size));
    }
}
