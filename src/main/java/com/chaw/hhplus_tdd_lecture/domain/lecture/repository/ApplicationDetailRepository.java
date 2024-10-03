package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;

public interface ApplicationDetailRepository {
    ApplicationDetail save(Long userId, Long lectureItemId);
    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);
}
