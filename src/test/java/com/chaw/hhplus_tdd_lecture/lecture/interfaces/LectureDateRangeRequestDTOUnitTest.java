package com.chaw.hhplus_tdd_lecture.lecture.interfaces;

import com.chaw.hhplus_tdd_lecture.interfaces.api.lecture.dto.LectureDateRangeRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LectureDateRangeRequestDTOUnitTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidDateRange() {
        LectureDateRangeRequestDTO dto = new LectureDateRangeRequestDTO(
                LocalDateTime.of(2023, 10, 1, 0, 0),
                LocalDateTime.of(2023, 10, 31, 23, 59)
        );

        Set<jakarta.validation.ConstraintViolation<LectureDateRangeRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty()); // 유효한 날짜 범위
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜와 동일한 경우")
    void testValidDateRangeSameDate() {
        LectureDateRangeRequestDTO dto = new LectureDateRangeRequestDTO(
                LocalDateTime.of(2023, 10, 1, 0, 0),
                LocalDateTime.of(2023, 10, 1, 23, 59)
        );

        Set<jakarta.validation.ConstraintViolation<LectureDateRangeRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty()); // 유효한 날짜 범위
    }

    @Test
    void testInvalidDateRange() {
        LectureDateRangeRequestDTO dto = new LectureDateRangeRequestDTO(
                LocalDateTime.of(2023, 11, 1, 0, 0),
                LocalDateTime.of(2023, 10, 31, 23, 59)
        );

        Set<jakarta.validation.ConstraintViolation<LectureDateRangeRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty()); // 유효하지 않은 날짜 범위
    }
}
