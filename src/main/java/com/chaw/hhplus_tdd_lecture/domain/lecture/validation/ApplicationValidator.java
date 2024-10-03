package com.chaw.hhplus_tdd_lecture.domain.lecture.validation;

import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ApplicationValidator {

    public void validate(User user, LectureItem lectureItem) {
        if (user.getType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("학생만 신청 가능합니다.");
        }

        if (LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1).isAfter(lectureItem.getDate())) {
            throw new IllegalArgumentException("내일 이후의 강의부터 가능합니다.");
        }

        if (lectureItem.getCapacity() <= lectureItem.getApplicants()) {
            throw new IllegalArgumentException("정원이 초과되었습니다.");
        }
    }
}
