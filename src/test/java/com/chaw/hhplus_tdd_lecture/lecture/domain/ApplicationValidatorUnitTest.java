package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.validation.ApplicationValidator;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationValidatorUnitTest {

    @InjectMocks
    private ApplicationValidator applicationValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void testValidate_ValidStudentAndValidLectureDate() {
        // Given
        User user = User.builder()
                .id(1L)
                .name("백")
                .type(User.UserType.STUDENT)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lectureId(101L)
                .date(LocalDateTime.now().plusDays(2))  // 2일 후의 강의
                .capacity(30)
                .applicants(10)
                .build();

        // When & Then
        assertDoesNotThrow(() -> applicationValidator.validate(user, lectureItem));
    }

    @Test
    void testValidate_NotStudent() {
        // Given
        User user = User.builder()
                .id(1L)
                .name("백")
                .type(User.UserType.INSTRUCTOR)  // 학생이 아님
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lectureId(101L)
                .date(LocalDateTime.now().plusDays(2))  // 2일 후의 강의
                .capacity(30)
                .applicants(10)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> applicationValidator.validate(user, lectureItem));

        assertEquals("학생만 신청 가능합니다.", exception.getMessage());
    }

    @Test
    void testValidate_InvalidLectureDate() {
        // Given
        User user = User.builder()
                .id(1L)
                .name("백")
                .type(User.UserType.STUDENT)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lectureId(101L)
                .date(LocalDateTime.now())  // 오늘 강의
                .capacity(30)
                .applicants(10)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> applicationValidator.validate(user, lectureItem));

        assertEquals("내일 이후의 강의부터 가능합니다.", exception.getMessage());
    }
}
