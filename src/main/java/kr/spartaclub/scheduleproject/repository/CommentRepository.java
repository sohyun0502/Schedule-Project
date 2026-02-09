package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Comment;
import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 한 일정 당 댓글 개수 count
    int countByScheduleId(Long scheduleId);

    // scheduleId로 해당 댓글 찾기
    List<Comment> findByScheduleId(Long scheduleId);

    // 유저별 전체 조회 (수정일 내림차순)
    List<Comment> findAllByUserIdOrderByModifiedAtDesc(Long userId);
}
