package com.chaw.hhplus_tdd_lecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId; // FK

    private Integer lectureItemId; // FK

    private LocalDateTime registrationDate;
}
