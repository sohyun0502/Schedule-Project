package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.dto.CreateScheduleRequest;
import kr.spartaclub.scheduleproject.dto.CreateScheduleResponse;
import kr.spartaclub.scheduleproject.dto.GetScheduleResponse;
import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse saveSchedule(CreateScheduleRequest request) {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getName(),
                request.getPassword()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getName(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    // 일정 조회 - 선택 일정 조회
    @Transactional(readOnly = true)
    public GetScheduleResponse findOneSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("없는 일정입니다.")
        );
        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 조회 - 전체 일정 조회
    public List<GetScheduleResponse> findAllSchedules(String name) {
        List<Schedule> schedules;
        if (name == null) {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        } else {
            schedules = scheduleRepository.findByNameOrderByModifiedAtDesc(name);
        }

        return schedules.stream().map(
                schedule -> new GetScheduleResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getName(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()
                )
        ).toList();

    }
}
