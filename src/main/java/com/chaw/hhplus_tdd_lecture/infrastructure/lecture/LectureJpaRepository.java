package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

}
