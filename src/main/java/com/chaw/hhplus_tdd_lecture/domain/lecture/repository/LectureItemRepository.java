package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;

import java.util.List;

public interface LectureItemRepository {

    LectureItem findById(Long lectureItemId);

    List<LectureItem> getLectureItemsByIds(List<Long> lectureItemIds);

    LectureItem save(LectureItem lectureItem);
}
