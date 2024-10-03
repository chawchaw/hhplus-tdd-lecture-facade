package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDetailJpaRepository extends JpaRepository<ApplicationDetail, Long> {

    List<ApplicationDetail> findByUserId(Long userId);

    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);
}
