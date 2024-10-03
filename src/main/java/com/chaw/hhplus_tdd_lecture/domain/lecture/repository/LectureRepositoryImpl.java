package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.infrastructure.lecture.LectureItemJpaRepository;
import com.chaw.hhplus_tdd_lecture.infrastructure.lecture.LectureJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;
    private final LectureItemJpaRepository lectureItemJpaRepository;

    public LectureRepositoryImpl(LectureJpaRepository lectureJpaRepository, LectureItemJpaRepository lectureItemJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
        this.lectureItemJpaRepository = lectureItemJpaRepository;
    }

    @Override
    public List<LectureItem> getApplicableLectureItems(LocalDateTime dateStart, LocalDateTime dateEnd) {
        return lectureItemJpaRepository.findByDateBetween(dateStart, dateEnd);
    }

    @Override
    public List<Lecture> getLecturesByIds(List<Long> lectureIds) {
        return lectureJpaRepository.findAllById(lectureIds);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }
}
