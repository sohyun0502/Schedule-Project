package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.dto.schedule.GetPageableScheduleResponse;
import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 전체 조회 (수정일 내림차순)
    List<Schedule> findAllByOrderByModifiedAtDesc();

    // 유저별 전체 조회 (수정일 내림차순)
    List<Schedule> findAllByUserIdOrderByModifiedAtDesc(Long userId);

    // 페이징된 일정 정보를 댓글 갯수와 함께 조회 (수정일 내림차순)
    @Query("""
    SELECT new kr.spartaclub.scheduleproject.dto.schedule.GetPageableScheduleResponse(
        s.id,
        s.title,
        s.content,
        COUNT(c.id),
        s.user.name,
        s.createdAt,
        s.modifiedAt
    )
    FROM Schedule s
    LEFT JOIN Comment c ON c.schedule.id = s.id
    GROUP BY s.id, s.title, s.content, s.user.name, s.createdAt, s.modifiedAt
    ORDER BY s.modifiedAt DESC
    """)
    Page<GetPageableScheduleResponse> findPageWithCommentCount(Pageable pageable);

    void deleteByUserId(Long userId);
}
