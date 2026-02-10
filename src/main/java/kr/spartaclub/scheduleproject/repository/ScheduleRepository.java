package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByOrderByModifiedAtDesc();

    // 유저별 전체 조회 (수정일 내림차순)
    List<Schedule> findAllByUserIdOrderByModifiedAtDesc(Long userId);

    Page<Schedule> findAll(Pageable pageable);

    void deleteByUserId(Long userId);
}
