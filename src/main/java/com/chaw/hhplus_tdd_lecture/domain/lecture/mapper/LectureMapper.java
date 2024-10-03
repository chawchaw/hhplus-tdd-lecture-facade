package com.chaw.hhplus_tdd_lecture.domain.lecture.mapper;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LectureMapper {

    private final ModelMapper modelMapper;

    public LectureMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LectureItemDTO mapToDTO(LectureItem lectureItem, List<User> users, List<Lecture> lectures) {
        LectureItemDTO dto = modelMapper.map(lectureItem, LectureItemDTO.class);
        Lecture lecture = findLectureById(lectureItem.getLectureId(), lectures);

        if (lecture != null) {
            dto.setLectureId(lecture.getId());
            dto.setTitle(lecture.getTitle());
            dto.setDescription(lecture.getDescription());

            User user = findUserById(lecture.getUserId(), users);
            if (user != null) {
                dto.setUserId(user.getId());
                dto.setInstructorName(user.getName());
            }
        }

        return dto;
    }

    public User findUserById(Long userId, List<User> users) {
        return users.stream()
                .filter(l -> l.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public Lecture findLectureById(Long lectureId, List<Lecture> lectures) {
        return lectures.stream()
                .filter(l -> l.getId().equals(lectureId))
                .findFirst()
                .orElse(null);
    }
}
