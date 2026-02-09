package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 유저별 전체 조회 (수정일 내림차순)
    @Query("select s from Schedule s where s.user.id = :userId order by s.modifiedAt desc")
    List<Schedule> findAllByOrderByModifiedAtDesc(@Param("userId") Long userId);

    // 작성자명으로 일정 조회 (수정일 내림차순)
    // List<Schedule> findByNameOrderByModifiedAtDesc(Long userId, String name);

}
