package com.chaw.hhplus_tdd_lecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "lecture_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lectureId; // FK

    private LocalDate date;

    private Integer capacity;

    private Integer applicants;
}
