package com.chaw.hhplus_tdd_lecture.domain.lecture.repository;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository {

    List<LectureItem> getApplicableLectureItems(LocalDateTime dateStart, LocalDateTime dateEnd);

    List<Lecture> getLecturesByIds(List<Long> lectureIds);

    Lecture save(Lecture lecture);
}
