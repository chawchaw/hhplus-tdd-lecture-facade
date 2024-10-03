package com.chaw.hhplus_tdd_lecture.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDetailDTO {

    private Long id;
    private Long userId;
    private LectureItemDTO lectureItem;
}
