package com.chaw.hhplus_tdd_lecture.infrastructure.lecture;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureItemJpaRepository extends JpaRepository<LectureItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LectureItem l WHERE l.id = :id")
    LectureItem findByIdWithLock(@Param("id") Long id);

    List<LectureItem> findByDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
}
