package com.chaw.hhplus_tdd_lecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // FK

    private String title;

    private String description;
}
