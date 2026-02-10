package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.comment.GetCommentResponse;
import kr.spartaclub.scheduleproject.dto.schedule.*;
import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentService commentService;
    private final UserRepository userRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse saveSchedule(Long userId, CreateScheduleRequest request) {
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

    // 일정 조회 - 일정 단건 조회
    @Transactional(readOnly = true)
    public GetOneScheduleResponse getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 선택한 일정의 댓글 리스트 조회
        List<Comment> commentList = commentService.getCommentList(id);
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

    // 일정 조회 - 일정 전체 조회
    @Transactional(readOnly = true)
    public List<GetAllScheduleResponse> getSchedules() {
        List<Schedule> schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();

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
        commentService.deleteCommentByScheduleId(id);
        scheduleRepository.deleteById(id);
    }

    // 페이징된 일정 조회
    @Transactional(readOnly = true)
    public Page<GetPageableScheduleResponse> getPageableSchedules(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);

        return schedules.map(
                schedule -> new GetPageableScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        commentService.countComment(schedule.getId()),
                        schedule.getUser().getName(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                )
        );
    }

    @Transactional
    public void deleteScheduleById(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUserIdOrderByModifiedAtDesc(userId);
        for (Schedule schedule : schedules) {
            commentService.deleteCommentByScheduleId(schedule.getId());
        }
        scheduleRepository.deleteByUserId(userId);
    }
}
