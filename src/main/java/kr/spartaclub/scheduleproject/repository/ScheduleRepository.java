package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 전체 조회 (수정일 내림차순)
    List<Schedule> findAllByOrderByModifiedAtDesc();

    // 작성자명으로 일정 조회 (수정일 내림차순)
    List<Schedule> findByNameOrderByModifiedAtDesc(String name);

}
