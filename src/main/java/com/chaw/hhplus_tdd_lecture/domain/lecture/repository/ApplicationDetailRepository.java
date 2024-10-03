package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;

import java.util.List;

public interface ApplicationDetailRepository {

    List<ApplicationDetail> getApplicationDetailsByUserId(Long userId);

    ApplicationDetail save(Long userId, Long lectureItemId);

    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);
}
