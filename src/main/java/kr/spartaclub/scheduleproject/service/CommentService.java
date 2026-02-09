package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.comment.CreateCommentRequest;
import kr.spartaclub.scheduleproject.dto.comment.CreateCommentResponse;
import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.CommentRepository;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CreateCommentResponse saveComment(Long userId, Long scheduleId, CreateCommentRequest request) {

        // 해당 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );

        // 해당 유저 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("없는 유저입니다.")
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
                user
        );

        // 저장
        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getUser().getName(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }

}
