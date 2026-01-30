package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.*;
import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.repository.CommentRepository;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse saveSchedule(CreateScheduleRequest request) {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getName(),
                request.getPassword()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getName(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    // 일정 조회 - 선택 일정 조회
    @Transactional(readOnly = true)
    public GetOneScheduleResponse findOneSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 선택한 일정의 댓글 리스트 조회
        List<Comment> commentList = commentRepository.findByScheduleId(id);
        List<GetCommentResponse> comments = commentList.stream().map(
                comment -> new GetCommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getName(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                )
        ).toList();

        return new GetOneScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                comments
        );
    }

    // 일정 조회 - 전체 일정 조회
    @Transactional(readOnly = true)
    public List<GetAllScheduleResponse> findAllSchedules(String name) {
        List<Schedule> schedules;
        if (name == null) {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        } else {
            schedules = scheduleRepository.findByNameOrderByModifiedAtDesc(name);
        }

        return schedules.stream().map(
                schedule -> new GetAllScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getName(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                )
        ).toList();

    }

    // 일정 수정
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long id, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 비밀번호 검증
        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        schedule.update(request.getTitle(), request.getName());
        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long id, DeleteScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 비밀번호 검증
        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(id);
    }

    // 댓글 생성
    @Transactional
    public CreateCommentResponse saveComment(Long scheduleId, CreateCommentRequest request) {
        // 해당 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 한 일정 당 댓글 개수 count
        int commentCount = commentRepository.countByScheduleId(scheduleId);

        // 댓글 10개 이상이면 예외처리
        if (commentCount >= 10) {
            throw new IllegalStateException("댓글은 최대 10개까지 작성할 수 있습니다.");
        }

        Comment comment = new Comment(
                schedule,
                request.getContent(),
                request.getName(),
                request.getPassword()
        );
        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getName(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }
}
