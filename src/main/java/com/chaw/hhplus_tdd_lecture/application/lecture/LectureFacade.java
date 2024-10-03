package com.chaw.hhplus_tdd_lecture.application.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.service.LectureService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LectureFacade {
    private final LectureService lectureService;

    public LectureFacade(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public Boolean application(Long userId, Long lectureItemId) {
        lectureService.application(userId, lectureItemId);
        return true;
    }

    public List<LectureItemDTO> getApplicableLecturesByDate(LocalDateTime dateStart, LocalDateTime dateEnd) {
        return lectureService.getApplicableLecturesByDate(dateStart, dateEnd);
    }
}
