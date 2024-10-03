package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;

public interface LectureItemRepository {
    LectureItem save(LectureItem lectureItem);
}
