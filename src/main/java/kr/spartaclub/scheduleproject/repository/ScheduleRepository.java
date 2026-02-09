package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 유저별 전체 조회 (수정일 내림차순)
    List<Schedule> findAllByUserIdOrderByModifiedAtDesc(@Param("userId") Long userId);
}
