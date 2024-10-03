package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectureMapperUnitTest {

    private LectureMapper lectureMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        lectureMapper = new LectureMapper(modelMapper);
    }

    @Test
    void testMapToDTO_WithUserAndLecture() {
        // Given
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lectureId(101L)
                .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();

        Lecture lecture = Lecture.builder()
                .id(101L)
                .userId(201L)
                .title("특강1")
                .description("특강1 설명")
                .build();

        User user = User.builder()
                .id(201L)
                .name("Back")
                .build();

        List<Lecture> lectures = Arrays.asList(lecture);
        List<User> users = Arrays.asList(user);

        // When
        LectureItemDTO dto = lectureMapper.mapToDTO(lectureItem, users, lectures);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(101L, dto.getLectureId());
        assertEquals("특강1", dto.getTitle());
        assertEquals("특강1 설명", dto.getDescription());
        assertEquals(201L, dto.getUserId());
        assertEquals("Back", dto.getInstructorName());
    }

    @Test
    void testMapToDTO_WithoutUserAndLecture() {
        // Given
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lectureId(999L)
                .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();

        List<Lecture> lectures = Arrays.asList();  // 강의가 없음
        List<User> users = Arrays.asList();  // 강사가 없음

        // When
        LectureItemDTO dto = lectureMapper.mapToDTO(lectureItem, users, lectures);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getUserId());
        assertNull(dto.getInstructorName());
    }

    @Test
    void testFindUserById_Found() {
        // Given
        List<User> users = Arrays.asList(
                User.builder().id(201L).name("백").build(),
                User.builder().id(202L).name("김").build()
        );

        // When
        User foundUser = lectureMapper.findUserById(201L, users);

        // Then
        assertNotNull(foundUser);
        assertEquals(201L, foundUser.getId());
        assertEquals("백", foundUser.getName());
    }

    @Test
    void testFindUserById_NotFound() {
        // Given
        List<User> users = Arrays.asList(
                User.builder().id(201L).name("백").build(),
                User.builder().id(202L).name("김").build()
        );

        // When
        User foundUser = lectureMapper.findUserById(999L, users);

        // Then
        assertNull(foundUser);
    }

    @Test
    void testFindLectureById_Found() {
        // Given
        List<Lecture> lectures = Arrays.asList(
                Lecture.builder().id(101L).title("Lecture 1").build(),
                Lecture.builder().id(102L).title("Lecture 2").build()
        );

        // When
        Lecture foundLecture = lectureMapper.findLectureById(101L, lectures);

        // Then
        assertNotNull(foundLecture);
        assertEquals(101L, foundLecture.getId());
        assertEquals("Lecture 1", foundLecture.getTitle());
    }

    @Test
    void testFindLectureById_NotFound() {
        // Given
        List<Lecture> lectures = Arrays.asList(
                Lecture.builder().id(101L).title("Lecture 1").build(),
                Lecture.builder().id(102L).title("Lecture 2").build()
        );

        // When
        Lecture foundLecture = lectureMapper.findLectureById(999L, lectures);

        // Then
        assertNull(foundLecture);
    }
}
