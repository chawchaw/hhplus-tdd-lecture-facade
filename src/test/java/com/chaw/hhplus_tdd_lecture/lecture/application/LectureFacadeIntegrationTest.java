package com.chaw.hhplus_tdd_lecture.lecture.application;

import com.chaw.hhplus_tdd_lecture.HhplusTddLectureApplication;
import com.chaw.hhplus_tdd_lecture.application.lecture.LectureFacade;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.ApplicationDetailRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureItemRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HhplusTddLectureApplication.class)
@ExtendWith(SpringExtension.class)
@Transactional
class LectureFacadeIntegrationTest {

    @Autowired
    private LectureFacade lectureFacade;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureItemRepository lectureItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationDetailRepository applicationDetailRepository;

    private User user;
    private Lecture lecture;
    private LectureItem lectureItem;
    private ApplicationDetail applicationDetail1;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 데이터 추가
        user = User.builder()
                .name("백")
                .type(User.UserType.STUDENT)
                .build();
        userRepository.save(user);

        lecture = Lecture.builder()
                .userId(user.getId())
                .title("특강1")
                .description("특강1 설명")
                .build();
        lectureRepository.save(lecture);

        lectureItem = LectureItem.builder()
                .lectureId(lecture.getId())
                .date(LocalDateTime.of(2024, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(lectureItem);

        applicationDetail1 = applicationDetailRepository.save(user.getId(), lectureItem.getId());
    }

    @Test
    void testApplication_Success() {
        // Given
        Long userId = user.getId();
        Long lectureItemId = lectureItem.getId();

        // When
        Boolean result = lectureFacade.application(userId, lectureItemId);

        // Then
        assertTrue(result);

        // LectureItem의 신청자 수가 증가했는지 확인
        LectureItem updatedLectureItem = lectureItemRepository.findById(lectureItemId);
        assertNotNull(updatedLectureItem);
        assertEquals(11, updatedLectureItem.getApplicants());  // 신청자가 증가했는지 확인
    }

    @Test
    void testApplication_ValidationFailure() {
        // Given
        Long userId = user.getId();

        // 오늘 날짜의 강의로 설정하여 validation을 실패시키기 위해 강의 데이터 생성
        LectureItem invalidLectureItem = LectureItem.builder()
                .lectureId(102L)
                .date(LocalDateTime.now())  // 오늘 날짜
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(invalidLectureItem);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> lectureFacade.application(userId, invalidLectureItem.getId()));

        // 신청자 수가 변경되지 않았는지 확인
        LectureItem unchangedLectureItem = lectureItemRepository.findById(invalidLectureItem.getId());
        assertNotNull(unchangedLectureItem);
        assertEquals(10, unchangedLectureItem.getApplicants());  // 신청자가 증가하지 않았는지 확인
    }

    @Test
    void testGetApplicableLecturesByDate() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2024, 10, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2024, 10, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureFacade.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(lectureItem.getId(), result.get(0).getId());
        assertEquals(lecture.getId(), result.get(0).getLectureId());
        assertEquals("특강1", result.get(0).getTitle());
        assertEquals("특강1 설명", result.get(0).getDescription());
        assertEquals(user.getId(), result.get(0).getUserId());
        assertEquals("백", result.get(0).getInstructorName());
    }

    @Test
    void testGetApplicableLecturesByDate_NoData() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2025, 12, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureFacade.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());  // 해당 날짜에 데이터가 없을 경우
    }

    @Test
    void testGetApplicationDetailsByUserId_Success() {
        // Given
        Long userId = user.getId();

        // When
        List<ApplicationDetailDTO> applicationDetailDTOs = lectureFacade.getApplicationDetailsByUserId(userId);

        // Then
        assertNotNull(applicationDetailDTOs);
        assertEquals(1, applicationDetailDTOs.size());  // 신청 내역이 1건이어야 함
        assertEquals(user.getId(), applicationDetailDTOs.get(0).getUserId());
        assertNotNull(applicationDetailDTOs.get(0).getLectureItem());
        assertEquals(lectureItem.getId(), applicationDetailDTOs.get(0).getLectureItem().getId());
        assertEquals("특강1", applicationDetailDTOs.get(0).getLectureItem().getTitle());
    }

    @Test
    void testGetApplicationDetailsByUserId_NoApplications() {
        // Given
        User newUser = User.builder()
                .name("백")
                .build();
        userRepository.save(newUser);

        // When
        List<ApplicationDetailDTO> applicationDetailDTOs = lectureFacade.getApplicationDetailsByUserId(newUser.getId());

        // Then
        assertNotNull(applicationDetailDTOs);
        assertTrue(applicationDetailDTOs.isEmpty());  // 신청 내역이 없는 경우
    }
}
