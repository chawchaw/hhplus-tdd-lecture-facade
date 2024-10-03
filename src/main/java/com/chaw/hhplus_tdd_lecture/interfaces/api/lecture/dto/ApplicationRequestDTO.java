package com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationRequestDTO {
    private Long userId;
    private Long lectureItemId;
}
