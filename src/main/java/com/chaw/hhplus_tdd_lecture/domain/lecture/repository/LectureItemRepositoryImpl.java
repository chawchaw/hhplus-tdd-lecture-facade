package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.infrastructure.lecture.LectureItemJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureItemRepositoryImpl implements LectureItemRepository {
    private final LectureItemJpaRepository lectureItemJpaRepository;

    public LectureItemRepositoryImpl(LectureItemJpaRepository lectureItemJpaRepository) {
        this.lectureItemJpaRepository = lectureItemJpaRepository;
    }

    @Override
    public LectureItem findByIdWithLock(long lectureItemId) {
        return lectureItemJpaRepository.findByIdWithLock(lectureItemId);
    }

    @Override
    public LectureItem findById(Long lectureItemId) {
        return lectureItemJpaRepository.findById(lectureItemId).orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));
    }

    @Override
    public List<LectureItem> getLectureItemsByIds(List<Long> lectureItemIds) {
        return lectureItemJpaRepository.findAllById(lectureItemIds);
    }

    @Override
    public LectureItem save(LectureItem lectureItem) {
        return lectureItemJpaRepository.save(lectureItem);
    }

    @Override
    public void deleteById(Long lectureItemId) {
        lectureItemJpaRepository.deleteById(lectureItemId);
    }

}
