package com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.validation;

import com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.LectureDateRangeRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, LectureDateRangeRequestDTO> {

    @Override
    public boolean isValid(LectureDateRangeRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getDateStart() == null || dto.getDateEnd() == null) {
            return true; // @NotNull이 처리하므로 여기서 검증하지 않음
        }
        return !dto.getDateStart().isAfter(dto.getDateEnd()); // 시작일이 종료일 이후가 아니어야 함
    }
}
