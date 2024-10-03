package com.chaw.hhplus_tdd_lecture.domain.lecture.mapper;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationDetailMapper {

    private final ModelMapper modelMapper;
    private final LectureMapper lectureMapper;

    public ApplicationDetailMapper(ModelMapper modelMapper, LectureMapper lectureMapper) {
        this.modelMapper = modelMapper;
        this.lectureMapper = lectureMapper;
    }

    public ApplicationDetailDTO mapToDTO(ApplicationDetail applicationDetail, List<LectureItem> lectureItems, List<User> users, List<Lecture> lectures) {
        ApplicationDetailDTO dto = modelMapper.map(applicationDetail, ApplicationDetailDTO.class);

        // LectureItem 매핑
        LectureItem lectureItem = lectureItems.stream()
                .filter(l -> l.getId().equals(applicationDetail.getLectureItemId()))
                .findFirst()
                .orElse(null);

        if (lectureItem != null) {
            LectureItemDTO lectureItemDTO = lectureMapper.mapToDTO(lectureItem, users, lectures);
            dto.setLectureItem(lectureItemDTO);
        }

        return dto;
    }
}
