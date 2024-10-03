package com.chaw.hhplus_tdd_lecture.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureItemDTO {
    // LectureItem
    private Long id;
    private LocalDate date;
    private Integer capacity;
    private Integer applicants;

    // Lecture
    private Long lectureId;
    private String title;
    private String description;

    // User
    private Long userId;
    private String instructorName;
}
