package com.chaw.hhplus_tdd_lecture.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserType type;

    public enum UserType {
        STUDENT, INSTRUCTOR
    }
}
