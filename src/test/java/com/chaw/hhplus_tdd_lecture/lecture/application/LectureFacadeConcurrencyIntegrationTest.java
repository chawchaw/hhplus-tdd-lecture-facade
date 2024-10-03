package com.chaw.hhplus_tdd_lecture.lecture.application;

import com.chaw.hhplus_tdd_lecture.HhplusTddLectureApplication;
import com.chaw.hhplus_tdd_lecture.application.lecture.LectureFacade;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.ApplicationDetailRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureItemRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = HhplusTddLectureApplication.class)
@ExtendWith(SpringExtension.class)
class LectureFacadeConcurrencyIntegrationTest {

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

    private List<User> students;
    private User instructor;
    private Lecture lecture;
    private LectureItem lectureItem;
    private List<ApplicationDetail> applicationDetails;

    @BeforeEach
    @Transactional
    void setUp() {
        // 학생 40명 생성 및 저장
        students = IntStream.range(0, 40)
                .mapToObj(i -> User.builder()
                        .name("학생 " + i)
                        .type(User.UserType.STUDENT)
                        .build())
                .collect(Collectors.toList());
        userRepository.saveAll(students);

        // 강사 생성 및 저장
        instructor = User.builder()
                .name("강사")
                .type(User.UserType.INSTRUCTOR)
                .build();
        userRepository.save(instructor);

        // 특강 생성 및 저장
        lecture = Lecture.builder()
                .title("특강")
                .description("강의 신청 동시성 테스트")
                .userId(instructor.getId())
                .build();
        lectureRepository.save(lecture);

        // 강의 항목 생성 및 저장 (정원 30명)
        lectureItem = LectureItem.builder()
                .lectureId(lecture.getId())
                .date(LocalDateTime.now().plusDays(1))
                .capacity(30)
                .applicants(0)
                .build();
        lectureItemRepository.save(lectureItem);
    }

    @Test
    @DisplayName("성공적으로 30명의 학생이 강의에 신청할 수 있다.")
    void testApplication_Success_WithinCapacity() {
        // 학생 ID 가져오기
        List<Long> studentIds = students.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // 30명의 학생이 강의를 신청
        List<CompletableFuture<Boolean>> futures = studentIds.subList(0, 30).stream()
                .map(studentId -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return lectureFacade.application(studentId, lectureItem.getId());
                    } catch (Exception e) {
                        return false;  // 실패한 경우 false 반환
                    }
                }))
                .collect(Collectors.toList());

        // 모든 쓰레드 완료 후 결과 모음
        List<Boolean> results = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
                .join();

        // 결과 검증: 모든 신청이 성공해야 함
        long successCount = results.stream().filter(Boolean::booleanValue).count();
        assertEquals(30, successCount);  // 30명 모두 성공

        // 최종 신청자 수 확인
        LectureItem updatedLectureItem = lectureItemRepository.findById(lectureItem.getId());
        assertEquals(30, updatedLectureItem.getApplicants());  // 신청자는 30명이어야 함
    }

    @Test
    @DisplayName("동시에 40명의 학생이 강의를 신청하면 10명은 정원 초과로 실패해야 한다.")
    void testApplication_Failure_ExceedApplicants() throws InterruptedException, ExecutionException {
        // 학생 ID 가져오기
        List<Long> studentIds = students.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // 40개의 쓰레드로 동시에 신청 처리
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (Long studentId : studentIds) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return lectureFacade.application(studentId, lectureItem.getId());
                } catch (Exception e) {
                    return false;  // 실패한 경우 false 반환
                }
            });
            futures.add(future);
        }

        // 모든 쓰레드 완료 후 결과 모음
        List<Boolean> results = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
                .get();

        // 결과 검증
        long successCount = results.stream().filter(result -> result).count();
        long failureCount = results.stream().filter(result -> !result).count();

        assertEquals(30, successCount);  // 성공한 학생 수
        assertEquals(10, failureCount);  // 실패한 학생 수

        // 최종 신청자 수 확인
        LectureItem updatedLectureItem = lectureItemRepository.findById(lectureItem.getId());
        assertEquals(30, updatedLectureItem.getApplicants());  // 신청자는 30명이어야 함
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // ApplicationDetail 삭제
        applicationDetailRepository.deleteByLectureItemId(lectureItem.getId());

        // LectureItem 삭제
        lectureItemRepository.deleteById(lectureItem.getId());

        // Lecture 삭제
        lectureRepository.deleteById(lecture.getId());

        // 학생과 강사 삭제
        userRepository.deleteByIds(students.stream().map(User::getId).collect(Collectors.toList()));
        userRepository.deleteById(instructor.getId());
    }
}
