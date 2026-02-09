package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.comment.GetCommentResponse;
import kr.spartaclub.scheduleproject.dto.schedule.*;
import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.CommentRepository;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse saveSchedule(Long userId, CreateScheduleRequest request) {

        // 유효성 체크
        // validateCreateSchedule(request);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
        );

        Schedule schedule = new Schedule(
                user,
                request.getTitle(),
                request.getContent()
        );

        // 저장
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getUser().getName(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    // 일정 생성시 유효성 체크
    /*private void validateCreateSchedule(CreateScheduleRequest request) {
        // 제목
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("일정 제목은 필수입니다.");
        }
        if (request.getTitle().length() > 30) {
            throw new IllegalArgumentException("일정 제목은 30자 이내여야 합니다.");
        }

        // 내용
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("일정 내용은 필수입니다.");
        }
        if (request.getContent().length() > 200) {
            throw new IllegalArgumentException("일정 내용은 200자 이내여야 합니다.");
        }

        // 작성자명
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("작성자명은 필수입니다.");
        }

        // 비밀번호
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }*/

    // 일정 조회 - 유저별 일정 단건 조회
    @Transactional(readOnly = true)
    public GetOneScheduleResponse getSchedule(Long userId, Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        if (!schedule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자와 로그인 유저가 다릅니다.");
        }

        // 선택한 일정의 댓글 리스트 조회
        List<Comment> commentList = commentRepository.findByScheduleId(id);
        List<GetCommentResponse> comments = commentList.stream().map(
                comment -> new GetCommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getName(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                )
        ).toList();

        return new GetOneScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                comments
        );
    }

    // 일정 조회 - 유저별 일정 전체 조회
    @Transactional(readOnly = true)
    public List<GetAllScheduleResponse> getSchedules(Long userId, String name) {
        List<Schedule> schedules = scheduleRepository.findAllByOrderByModifiedAtDesc(userId);

        return schedules.stream().map(
                schedule -> new GetAllScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getUser().getName(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                )
        ).toList();

    }

    // 일정 수정
    // 수정 시 validation 체크 필요
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long userId, Long id, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 유효성 체크
        // validateUpdateSchedule(request);

        // 작성자와 로그인 유저가 같은지 비교
        if (!schedule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자와 로그인 유저가 다릅니다.");
        }

        // 제목만 수정
        schedule.update(request.getTitle(), request.getContent());
        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 수정시 유효성 체크
    /*private void validateUpdateSchedule(UpdateScheduleRequest request) {
        // 제목
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("일정 제목은 필수입니다.");
        }
        if (request.getTitle().length() > 30) {
            throw new IllegalArgumentException("일정 제목은 30자 이내여야 합니다.");
        }

        // 작성자명
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("작성자명은 필수입니다.");
        }
    }*/

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long userId, Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 작성자와 로그인 유저가 같은지 비교
        if (!schedule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자와 로그인 유저가 다릅니다.");
        }

        // 삭제
        scheduleRepository.deleteById(id);
    }
}
