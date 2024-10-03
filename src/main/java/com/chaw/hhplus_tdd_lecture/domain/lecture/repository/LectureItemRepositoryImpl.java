package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.infrastructure.lecture.LectureItemJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LectureItemRepositoryImpl implements LectureItemRepository {
    private final LectureItemJpaRepository lectureItemJpaRepository;

    public LectureItemRepositoryImpl(LectureItemJpaRepository lectureItemJpaRepository) {
        this.lectureItemJpaRepository = lectureItemJpaRepository;
    }

    @Override
    public LectureItem save(LectureItem lectureItem) {
        return lectureItemJpaRepository.save(lectureItem);
    }
}
