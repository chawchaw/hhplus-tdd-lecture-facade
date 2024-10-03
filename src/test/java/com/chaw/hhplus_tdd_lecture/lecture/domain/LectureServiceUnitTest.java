package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.ApplicationDetailMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.ApplicationDetailRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureItemRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.service.LectureService;
import com.chaw.hhplus_tdd_lecture.domain.lecture.validation.ApplicationValidator;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

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
    private LectureItemRepository lectureItemRepository;

    @Mock
    private ApplicationDetailRepository applicationDetailRepository;

    @Mock
    private ApplicationValidator applicationValidator;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureMapper lectureMapper;

    @Mock
    private ApplicationDetailMapper applicationDetailMapper;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void testApplication_Success() {
        // Given
        Long userId = 1L;
        Long lectureItemId = 10L;

        User user = User.builder()
                .id(userId)
                .name("백")
                .type(User.UserType.STUDENT)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(lectureItemId)
                .lectureId(101L)
                .capacity(30)
                .applicants(10)
                .build();

        ApplicationDetail applicationDetail = ApplicationDetail.builder()
                .userId(userId)
                .lectureItemId(lectureItemId)
                .registrationDate(LocalDateTime.now())
                .build();

        // Mock behavior
        when(userService.getUserById(userId)).thenReturn(user);
        when(lectureItemRepository.findByIdWithLock(lectureItemId)).thenReturn(lectureItem);
        when(applicationDetailRepository.existsByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(false);
        when(applicationDetailRepository.save(userId, lectureItemId)).thenReturn(applicationDetail);

        // When
        ApplicationDetail result = lectureService.application(userId, lectureItemId);

        // Then
        assertEquals(userId, result.getUserId());
        assertEquals(lectureItemId, result.getLectureItemId());
        verify(userService, times(1)).getUserById(userId);
        verify(lectureItemRepository, times(1)).findByIdWithLock(lectureItemId);
        verify(applicationValidator, times(1)).validate(user, lectureItem, false);
        verify(lectureItemRepository, times(1)).save(lectureItem);
        verify(applicationDetailRepository, times(1)).save(userId, lectureItemId);
    }

    @Test
    void testApplication_ValidationFailure() {
        // Given
        Long userId = 1L;
        Long lectureItemId = 10L;

        User user = User.builder()
                .id(userId)
                .name("백")
                .type(User.UserType.STUDENT)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(lectureItemId)
                .lectureId(101L)
                .capacity(30)
                .applicants(10)
                .build();

        // Mock behavior
        when(userService.getUserById(userId)).thenReturn(user);
        when(lectureItemRepository.findByIdWithLock(lectureItemId)).thenReturn(lectureItem);

        // Validator throws exception
        doThrow(new IllegalArgumentException("Validation failed")).when(applicationValidator).validate(user, lectureItem, false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> lectureService.application(userId, lectureItemId));

        // Verify that other operations were not performed
        verify(lectureItemRepository, never()).save(lectureItem);
        verify(applicationDetailRepository, never()).save(anyLong(), anyLong());
    }

    @Test
    @DisplayName("이미 신청한 경우 데이터 무결성 예외가 발생할 때 적절한 메시지를 던진다.")
    void testApplication_DataIntegrityViolationException() {
        Long userId = 1L;
        Long lectureItemId = 1L;

        User user = User.builder().id(userId).name("John").build();
        LectureItem lectureItem = LectureItem.builder().id(lectureItemId).applicants(10).capacity(20).build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(lectureItemRepository.findByIdWithLock(lectureItemId)).thenReturn(lectureItem);
        when(applicationDetailRepository.existsByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(false);
        when(applicationDetailRepository.save(userId, lectureItemId)).thenThrow(new DataIntegrityViolationException("이미 신청한 강의입니다."));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            lectureService.application(userId, lectureItemId);
        });

        assertEquals("이미 신청한 강의입니다.", exception.getMessage());
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
                        .title("특강1")
                        .description("특강1 설명")
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
                .title("특강1")
                .description("특강1 설명")
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
        assertEquals("특강1", result.get(0).getTitle());
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

    @Test
    void testGetApplicationDetailsByUserId_Success() {
        // Given
        Long userId = 1L;

        ApplicationDetail applicationDetail = ApplicationDetail.builder()
                .id(1L)
                .userId(userId)
                .lectureItemId(2L)
                .build();

        LectureItem lectureItem = LectureItem.builder()
                .id(2L)
                .lectureId(3L)
                .capacity(30)
                .applicants(10)
                .build();

        Lecture lecture = Lecture.builder()
                .id(3L)
                .userId(4L)
                .title("Sample Lecture")
                .description("Sample Description")
                .build();

        User user = User.builder()
                .id(4L)
                .name("백")
                .build();

        ApplicationDetailDTO applicationDetailDTO = ApplicationDetailDTO.builder()
                .id(1L)
                .userId(userId)
                .lectureItem(null)
                .build();

        // Mocking 리포지토리 응답
        when(applicationDetailRepository.getApplicationDetailsByUserId(userId)).thenReturn(Arrays.asList(applicationDetail));
        when(lectureItemRepository.getLectureItemsByIds(Arrays.asList(2L))).thenReturn(Arrays.asList(lectureItem));
        when(lectureRepository.getLecturesByIds(Arrays.asList(3L))).thenReturn(Arrays.asList(lecture));
        when(userService.getUsersByIds(Arrays.asList(4L))).thenReturn(Arrays.asList(user));
        when(applicationDetailMapper.mapToDTO(any(ApplicationDetail.class), anyList(), anyList(), anyList())).thenReturn(applicationDetailDTO);

        // When
        List<ApplicationDetailDTO> result = lectureService.getApplicationDetailsByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(userId, result.get(0).getUserId());

        // Verify 리포지토리 메서드 호출 횟수
        verify(applicationDetailRepository, times(1)).getApplicationDetailsByUserId(userId);
        verify(lectureItemRepository, times(1)).getLectureItemsByIds(anyList());
        verify(lectureRepository, times(1)).getLecturesByIds(anyList());
        verify(userService, times(1)).getUsersByIds(anyList());
        verify(applicationDetailMapper, times(1)).mapToDTO(any(ApplicationDetail.class), anyList(), anyList(), anyList());
    }
}
