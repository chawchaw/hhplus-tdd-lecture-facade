package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.infrastructure.lecture.ApplicationDetailJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ApplicationDetailRepositoryImpl implements ApplicationDetailRepository {

    private final ApplicationDetailJpaRepository repository;

    public ApplicationDetailRepositoryImpl(ApplicationDetailJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ApplicationDetail save(Long userId, Long lectureItemId) {
        ApplicationDetail applicationDetail = ApplicationDetail.builder()
                .userId(userId)
                .lectureItemId(lectureItemId)
                .registrationDate(LocalDateTime.now())
                .build();
        return repository.save(applicationDetail);
    }

    @Override
    public boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId) {
        return repository.existsByUserIdAndLectureItemId(userId, lectureItemId);
    }
}
