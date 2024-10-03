package com.chaw.hhplus_tdd_lecture.lecture.domain;

import com.chaw.hhplus_tdd_lecture.HhplusTddLectureApplication;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
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

    private User user1;
    private Lecture lecture1;
    private LectureItem lectureItem1;

    @BeforeEach
    void setUp() {
        // 데이터베이스에 필요한 테스트 데이터 삽입
        user1 = User.builder()
                .name("백")
                .build();
        userRepository.save(user1);

        lecture1 = Lecture.builder()
                .userId(user1.getId())
                .title("Lecture Title 1")
                .description("Lecture Description 1")
                .build();
        lectureRepository.save(lecture1);

        lectureItem1 = LectureItem.builder()
                .lectureId(lecture1.getId())
                .date(LocalDateTime.of(2023, 10, 10, 10, 0))
                .capacity(30)
                .applicants(10)
                .build();
        lectureItemRepository.save(lectureItem1);
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
        assertEquals("Lecture Title 1", result.get(0).getTitle());
        assertEquals("Lecture Description 1", result.get(0).getDescription());
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
}
