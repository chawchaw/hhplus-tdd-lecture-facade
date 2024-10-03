package com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto;

import com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.validation.ValidDateRange;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidDateRange
@AllArgsConstructor
public class LectureDateRangeRequestDTO {
    @NotNull(message = "시작일은 필수입니다.")
    private LocalDateTime dateStart;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDateTime dateEnd;
}
