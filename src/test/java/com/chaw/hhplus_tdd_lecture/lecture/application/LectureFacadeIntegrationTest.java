package com.chaw.hhplus_tdd_lecture.lecture.application;

import com.chaw.hhplus_tdd_lecture.HhplusTddLectureApplication;
import com.chaw.hhplus_tdd_lecture.application.lecture.LectureFacade;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
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

    private User user;
    private Lecture lecture;
    private LectureItem lectureItem;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 데이터 추가
        user = User.builder()
                .name("백")
                .build();
        userRepository.save(user);

        lecture = Lecture.builder()
                .userId(user.getId())
                .title("Lecture Title")
                .description("Lecture Description")
                .build();
        lectureRepository.save(lecture);

        lectureItem = LectureItem.builder()
                .lectureId(lecture.getId())
                .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(lectureItem);
    }

    @Test
    void testGetApplicableLecturesByDate() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2023, 10, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureFacade.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(lectureItem.getId(), result.get(0).getId());
        assertEquals(lecture.getId(), result.get(0).getLectureId());
        assertEquals("Lecture Title", result.get(0).getTitle());
        assertEquals("Lecture Description", result.get(0).getDescription());
        assertEquals(user.getId(), result.get(0).getUserId());
        assertEquals("백", result.get(0).getInstructorName());
    }

    @Test
    void testGetApplicableLecturesByDate_NoData() {
        // Given
        LocalDateTime dateStart = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        List<LectureItemDTO> result = lectureFacade.getApplicableLecturesByDate(dateStart, dateEnd);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());  // 해당 날짜에 데이터가 없을 경우
    }
}
