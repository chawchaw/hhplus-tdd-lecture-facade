package com.chaw.hhplus_tdd_lecture.interfaces.api.lecture;

import com.chaw.hhplus_tdd_lecture.application.lecture.LectureFacade;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.ApplicationRequestDTO;
import com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.LectureDateRangeRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureFacade lectureFacade;

    public LectureController(LectureFacade lectureFacade) {
        this.lectureFacade = lectureFacade;
    }

    @PostMapping("/application")
    public ResponseEntity<Boolean> application(
            @RequestBody ApplicationRequestDTO requestDTO) {
        lectureFacade.application(requestDTO.getUserId(), requestDTO.getLectureItemId());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/applicable")
    public ResponseEntity<List<LectureItemDTO>> getApplicableLectures(
            @Valid @RequestBody LectureDateRangeRequestDTO dateRange) {
        List<LectureItemDTO> lectureItemDTOs = lectureFacade.getApplicableLecturesByDate(
                dateRange.getDateStart(), dateRange.getDateEnd());
        return ResponseEntity.ok(lectureItemDTOs);
    }
}
