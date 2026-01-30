package kr.spartaclub.scheduleproject.repository;

import kr.spartaclub.scheduleproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
