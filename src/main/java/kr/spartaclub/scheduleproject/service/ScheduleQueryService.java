package kr.spartaclub.scheduleproject.service;

import kr.spartaclub.scheduleproject.entity.Schedule;
import kr.spartaclub.scheduleproject.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public Schedule getScheduleByIdOrThrow(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalStateException("일정이 없습니다."));
    }
}
