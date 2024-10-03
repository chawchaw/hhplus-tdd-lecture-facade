package com.chaw.hhplus_tdd_lecture.domain.lecture.service;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureService {

    private final UserService userService;
    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;

    public LectureService(UserService userService, LectureRepository lectureRepository, LectureMapper lectureMapper) {
        this.userService = userService;
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
    }

    public List<LectureItemDTO> getApplicableLecturesByDate(LocalDateTime dateStart, LocalDateTime dateEnd) {
        List<LectureItem> lectureItems = lectureRepository.getApplicableLectureItems(dateStart, dateEnd);
        List<Long> lectureIds = lectureItems.stream().map(LectureItem::getLectureId).collect(Collectors.toList());
        List<Lecture> lectures = lectureRepository.getLecturesByIds(lectureIds);
        List<Long> userIds = lectures.stream().map(Lecture::getUserId).collect(Collectors.toList());
        List<User> users = userService.getUsersByIds(userIds);

        return lectureItems.stream()
                .map(lectureItem -> lectureMapper.mapToDTO(lectureItem, users, lectures))
                .collect(Collectors.toList());
    }

}
