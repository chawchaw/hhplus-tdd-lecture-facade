package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.ApplicationDetailMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationDetailMapperUnitTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LectureMapper lectureMapper;

    @InjectMocks
    private ApplicationDetailMapper applicationDetailMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapToDTO_Success() {
        // Given
        ApplicationDetail applicationDetail = ApplicationDetail.builder()
                .id(1L)
                .userId(2L)
                .lectureItemId(3L)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(3L)
                .lectureId(101L)
                .date(LocalDateTime.now())
                .capacity(30)
                .applicants(10)
                .build();

        User user = User.builder()
                .id(2L)
                .name("백")
                .build();

        Lecture lecture = Lecture.builder()
                .id(101L)
                .title("Sample Lecture")
                .description("특강1 설명")
                .userId(user.getId())
                .build();

        List<LectureItem> lectureItems = Arrays.asList(lectureItem);
        List<User> users = Arrays.asList(user);
        List<Lecture> lectures = Arrays.asList(lecture);

        ApplicationDetailDTO mockDTO = ApplicationDetailDTO.builder()
                .id(1L)
                .userId(2L)
                .lectureItem(new LectureItemDTO()) // 기본 DTO 설정
                .build();

        LectureItemDTO lectureItemDTO = LectureItemDTO.builder()
                .id(3L)
                .lectureId(101L)
                .title("Sample Lecture")
                .description("특강1 설명")
                .userId(2L)
                .instructorName("백")
                .build();

        // Mock behavior
        when(modelMapper.map(applicationDetail, ApplicationDetailDTO.class)).thenReturn(mockDTO);
        when(lectureMapper.mapToDTO(lectureItem, users, lectures)).thenReturn(lectureItemDTO);

        // When
        ApplicationDetailDTO result = applicationDetailMapper.mapToDTO(applicationDetail, lectureItems, users, lectures);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2L, result.getUserId());
        assertNotNull(result.getLectureItem());
        assertEquals(3L, result.getLectureItem().getId());
        assertEquals("Sample Lecture", result.getLectureItem().getTitle());
        assertEquals("백", result.getLectureItem().getInstructorName());

        // Verify interactions
        verify(modelMapper, times(1)).map(applicationDetail, ApplicationDetailDTO.class);
        verify(lectureMapper, times(1)).mapToDTO(lectureItem, users, lectures);
    }

    @Test
    void testMapToDTO_LectureItemNotFound() {
        // Given
        ApplicationDetail applicationDetail = ApplicationDetail.builder()
                .id(1L)
                .userId(2L)
                .lectureItemId(999L)  // 존재하지 않는 LectureItem ID
                .build();

        List<LectureItem> lectureItems = Arrays.asList(); // 빈 리스트
        List<User> users = Arrays.asList();
        List<Lecture> lectures = Arrays.asList();

        ApplicationDetailDTO mockDTO = ApplicationDetailDTO.builder()
                .id(1L)
                .userId(2L)
                .build();

        // Mock behavior
        when(modelMapper.map(applicationDetail, ApplicationDetailDTO.class)).thenReturn(mockDTO);

        // When
        ApplicationDetailDTO result = applicationDetailMapper.mapToDTO(applicationDetail, lectureItems, users, lectures);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2L, result.getUserId());
        assertNull(result.getLectureItem());  // LectureItem이 없으므로 null이어야 함

        // Verify interactions
        verify(modelMapper, times(1)).map(applicationDetail, ApplicationDetailDTO.class);
        verify(lectureMapper, never()).mapToDTO(any(), any(), any());  // LectureItem이 없으므로 호출되지 않음
    }
}
