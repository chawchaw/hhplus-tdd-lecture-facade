package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.HhplusTddLectureApplication;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.ApplicationDetailRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureItemRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.service.LectureService;
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
public class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureItemRepository lectureItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationDetailRepository applicationDetailRepository;

    private User user1;
    private Lecture lecture1;
    private LectureItem lectureItem1;
    private ApplicationDetail applicationDetail1;

    @BeforeEach
    void setUp() {
        // 데이터베이스에 필요한 테스트 데이터 삽입
        user1 = User.builder()
                .name("백")
                .type(User.UserType.STUDENT)
                .build();
        userRepository.save(user1);

        lecture1 = Lecture.builder()
                .userId(user1.getId())
                .title("특강1")
                .description("특강1 설명")
                .build();
        lectureRepository.save(lecture1);

        lectureItem1 = LectureItem.builder()
                .lectureId(lecture1.getId())
                .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(lectureItem1);

        applicationDetail1 = applicationDetailRepository.save(user1.getId(), lectureItem1.getId());
    }

    @Test
    void testApplication_Success() {
        // Given
        Long userId = user1.getId();
        Long lectureId = lecture1.getId();

        LectureItem lectureItem = LectureItem.builder()
                .lectureId(lectureId)
                .date(LocalDateTime.now().plusDays(1))  // 오늘 날짜
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(lectureItem);

        // When
        ApplicationDetail result = lectureService.application(userId, lectureItem.getId());

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(lectureItem.getId(), result.getLectureItemId());

        LectureItem updatedLectureItem = lectureItemRepository.findById(lectureItem.getId());
        assertNotNull(updatedLectureItem);
        assertEquals(11, updatedLectureItem.getApplicants());  // 신청자가 증가했는지 확인

        boolean applicationExists = applicationDetailRepository.existsByUserIdAndLectureItemId(userId, lectureItem.getId());
        assertTrue(applicationExists);  // 신청 내역이 저장되었는지 확인
    }

    @Test
    void testApplication_ValidationFailure() {
        // Given
        Long userId = user1.getId();
        Long lectureId = lecture1.getId();

        // Validation 실패를 위해 내일이 아닌 오늘 강의를 생성
        LectureItem invalidLectureItem = LectureItem.builder()
                .lectureId(lectureId)
                .date(LocalDateTime.now())  // 오늘 날짜
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(invalidLectureItem);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> lectureService.application(userId, invalidLectureItem.getId()));

        // 신청 내역 및 신청자 수가 변경되지 않았는지 확인
        LectureItem unchangedLectureItem = lectureItemRepository.findById(invalidLectureItem.getId());
        assertNotNull(unchangedLectureItem);
        assertEquals(10, unchangedLectureItem.getApplicants());  // 신청자가 변경되지 않음

        boolean applicationExists = applicationDetailRepository.existsByUserIdAndLectureItemId(userId, invalidLectureItem.getId());
        assertFalse(applicationExists);  // 신청 내역이 저장되지 않았는지 확인
    }

    @Test
    void testGetApplicableLecturesByDate() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2023, 10, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureService.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(lectureItem1.getId(), result.get(0).getId());
        assertEquals(lecture1.getId(), result.get(0).getLectureId());
        assertEquals("특강1", result.get(0).getTitle());
        assertEquals("특강1 설명", result.get(0).getDescription());
        assertEquals(user1.getId(), result.get(0).getUserId());
        assertEquals("백", result.get(0).getInstructorName());
    }

    @Test
    void testGetApplicableLecturesByDate_NoData() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureService.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());  // 해당 날짜에 데이터가 없는 경우
    }

    @Test
    void testGetApplicationDetailsByUserId_Success() {
        // Given
        Long userId = user1.getId();

        // When
        List<ApplicationDetailDTO> applicationDetailDTOs = lectureService.getApplicationDetailsByUserId(userId);

        // Then
        assertNotNull(applicationDetailDTOs);
        assertEquals(1, applicationDetailDTOs.size());  // 데이터가 하나만 있어야 함
        assertEquals(user1.getId(), applicationDetailDTOs.get(0).getUserId());
        assertNotNull(applicationDetailDTOs.get(0).getLectureItem());
        assertEquals(lectureItem1.getId(), applicationDetailDTOs.get(0).getLectureItem().getId());
        assertEquals("특강1", applicationDetailDTOs.get(0).getLectureItem().getTitle());
        assertEquals("백", applicationDetailDTOs.get(0).getLectureItem().getInstructorName());
    }

    @Test
    void testGetApplicationDetailsByUserId_NoApplications() {
        // Given
        User newUser = User.builder()
                .name("백")
                .build();
        userRepository.save(newUser);

        // When
        List<ApplicationDetailDTO> applicationDetailDTOs = lectureService.getApplicationDetailsByUserId(newUser.getId());

        // Then
        assertNotNull(applicationDetailDTOs);
        assertTrue(applicationDetailDTOs.isEmpty());  // 새로운 사용자이므로 신청 내역이 없어야 함
    }
}
