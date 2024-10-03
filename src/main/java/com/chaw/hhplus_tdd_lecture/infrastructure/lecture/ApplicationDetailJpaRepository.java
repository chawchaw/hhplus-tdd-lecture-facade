package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDetailJpaRepository extends JpaRepository<ApplicationDetail, Long> {

    List<ApplicationDetail> findByUserId(Long userId);

    List<ApplicationDetail> findByUserIdAndLectureItemId(Long userId, Long lectureItemId);

    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ApplicationDetail a WHERE a.lectureItemId = :lectureItemId")
    void deleteByLectureItemId(Long lectureItemId);
}
