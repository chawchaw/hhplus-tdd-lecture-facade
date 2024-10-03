package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureItemJpaRepository extends JpaRepository<LectureItem, Long> {
    List<LectureItem> findByDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
}
