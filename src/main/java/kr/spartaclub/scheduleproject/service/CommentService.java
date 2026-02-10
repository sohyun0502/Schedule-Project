package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.comment.CreateCommentRequest;
import kr.spartaclub.scheduleproject.dto.comment.CreateCommentResponse;
import kr.spartaclub.scheduleproject.dto.comment.GetCommentResponse;
import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.entity.User;
import kr.spartaclub.scheduleproject.repository.CommentRepository;
import kr.spartaclub.scheduleproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleQueryService scheduleQueryService;

    // 댓글 생성
    @Transactional
    public CreateCommentResponse saveComment(Long userId, Long scheduleId, CreateCommentRequest request) {

        // 해당 일정 조회
        Schedule schedule = scheduleQueryService.getScheduleByIdOrThrow(scheduleId);

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

    @Transactional(readOnly = true)
    public List<GetCommentResponse> getComments(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserIdOrderByModifiedAtDesc(userId);
        return comments.stream().map(
                comment -> new GetCommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getName(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt()
                )
        ).toList();
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentList(Long id) {
        return commentRepository.findByScheduleId(id);
    }

    @Transactional(readOnly = true)
    public int countComment(Long scheduleId) {
        return commentRepository.countByScheduleId(scheduleId);
    }

    @Transactional
    public void deleteCommentByUserId(Long userId) {
        commentRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteCommentByScheduleId(Long scheduleId) {
        commentRepository.deleteByScheduleId(scheduleId);
    }
}
