package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDetailJpaRepository extends JpaRepository<ApplicationDetail, Long> {
    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);
}
