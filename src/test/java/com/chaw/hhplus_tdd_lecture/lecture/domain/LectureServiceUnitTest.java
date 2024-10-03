package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.service.LectureService;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LectureServiceUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureMapper lectureMapper;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void testGetApplicableLecturesByDate() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2023, 10, 31, 23, 59);

        List<LectureItem> mockLectureItems = Arrays.asList(
                LectureItem.builder()
                        .id(1L)
                        .lectureId(101L)
                        .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                        .capacity(30)
                        .applicants(10)
                        .build()
        );

        List<Lecture> mockLectures = Arrays.asList(
                Lecture.builder()
                        .id(101L)
                        .userId(201L)
                        .title("Lecture Title")
                        .description("Lecture Description")
                        .build()
        );

        List<User> mockUsers = Arrays.asList(
                User.builder()
                        .id(201L)
                        .name("백")
                        .build()
        );

        LectureItemDTO mockLectureItemDTO = LectureItemDTO.builder()
                .id(1L)
                .lectureId(101L)
                .title("Lecture Title")
                .description("Lecture Description")
                .userId(201L)
                .instructorName("백")
                .date(LocalDate.of(2023, 10, 10))
                .capacity(30)
                .applicants(10)
                .build();

        // Mock behavior
        when(lectureRepository.getApplicableLectureItems(dateStart, dateEnd)).thenReturn(mockLectureItems);
        when(lectureRepository.getLecturesByIds(Arrays.asList(101L))).thenReturn(mockLectures);
        when(userService.getUsersByIds(Arrays.asList(201L))).thenReturn(mockUsers);
        when(lectureMapper.mapToDTO(mockLectureItems.get(0), mockUsers, mockLectures)).thenReturn(mockLectureItemDTO);

        // When
        List<LectureItemDTO> result = lectureService.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getLectureId());
        assertEquals("Lecture Title", result.get(0).getTitle());
        assertEquals(201L, result.get(0).getUserId());
        assertEquals("백", result.get(0).getInstructorName());

        // Verify interactions with the mocks
        verify(lectureRepository, times(1)).getApplicableLectureItems(dateStart, dateEnd);
        verify(lectureRepository, times(1)).getLecturesByIds(Arrays.asList(101L));
        verify(userService, times(1)).getUsersByIds(Arrays.asList(201L));
        verify(lectureMapper, times(1)).mapToDTO(mockLectureItems.get(0), mockUsers, mockLectures);
    }

    @Test
    void testGetApplicableLecturesByDate_NoData() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2024, 12, 31, 23, 59);

        // Mock behavior
        when(lectureRepository.getApplicableLectureItems(dateStart, dateEnd)).thenReturn(Arrays.asList());

        // When
        List<LectureItemDTO> result = lectureService.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());  // 해당 날짜에 데이터가 없는 경우

        // Verify interactions with the mocks
        verify(lectureRepository, times(1)).getApplicableLectureItems(dateStart, dateEnd);
        verify(lectureRepository, times(1)).getLecturesByIds(anyList());
        verify(userService, times(1)).getUsersByIds(anyList());
        verify(lectureMapper, never()).mapToDTO(any(LectureItem.class), anyList(), anyList());
    }
}
