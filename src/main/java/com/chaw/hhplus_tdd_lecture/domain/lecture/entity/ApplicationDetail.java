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
    private Long id;

    private Long userId; // FK

    private Long lectureItemId; // FK

    private LocalDateTime registrationDate;
}
