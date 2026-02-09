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

        // 유효성 체크
        // validateCreateComment(request);

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

    // 댓글 생성시 유효성 체크
    /*private void validateCreateComment(CreateCommentRequest request) {
        // 내용
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }
        if (request.getContent().length() > 100) {
            throw new IllegalArgumentException("댓글 내용은 100자 이내여야 합니다.");
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
}
