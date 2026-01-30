package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 한 일정 당 댓글 개수 count
    int countByScheduleId(Long scheduleId);
}
