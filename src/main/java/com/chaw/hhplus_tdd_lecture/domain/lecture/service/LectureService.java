package com.chaw.hhplus_tdd_lecture.domain.lecture.service;

import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.ApplicationDetailDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.dto.LectureItemDTO;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.ApplicationDetail;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.Lecture;
import com.chaw.hhplus_tdd_lecture.domain.lecture.entity.LectureItem;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.ApplicationDetailMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.mapper.LectureMapper;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.ApplicationDetailRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureItemRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.repository.LectureRepository;
import com.chaw.hhplus_tdd_lecture.domain.lecture.validation.ApplicationValidator;
import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureService {

    private final UserService userService;
    private final LectureRepository lectureRepository;
    private final LectureItemRepository lectureItemRepository;
    private final ApplicationDetailRepository applicationDetailRepository;
    private final ApplicationValidator applicationValidator;
    private final LectureMapper lectureMapper;
    private final ApplicationDetailMapper applicationDetailMapper;

    public LectureService(UserService userService, LectureRepository lectureRepository, LectureItemRepository lectureItemRepository, ApplicationDetailRepository applicationDetailRepository, ApplicationValidator applicationValidator, LectureMapper lectureMapper, ApplicationDetailMapper applicationDetailMapper) {
        this.userService = userService;
        this.lectureRepository = lectureRepository;
        this.lectureItemRepository = lectureItemRepository;
        this.applicationDetailRepository = applicationDetailRepository;
        this.applicationValidator = applicationValidator;
        this.lectureMapper = lectureMapper;
        this.applicationDetailMapper = applicationDetailMapper;
    }

    @Transactional
    public ApplicationDetail application(Long userId, Long lectureItemId) {
        User user = userService.getUserById(userId);
        LectureItem lectureItem = lectureItemRepository.findByIdWithLock(lectureItemId);
        applicationValidator.validate(user, lectureItem);
        lectureItem.setApplicants(lectureItem.getApplicants() + 1);
        lectureItemRepository.save(lectureItem);
        ApplicationDetail applicationDetail = applicationDetailRepository.save(userId, lectureItemId);
        return applicationDetail;
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

    public List<ApplicationDetailDTO> getApplicationDetailsByUserId(Long userId) {
        List<ApplicationDetail> applicationDetails = applicationDetailRepository.getApplicationDetailsByUserId(userId);
        List<Long> lectureItemIds = applicationDetails.stream().map(ApplicationDetail::getLectureItemId).collect(Collectors.toList());
        List<LectureItem> lectureItems = lectureItemRepository.getLectureItemsByIds(lectureItemIds);
        List<Long> lectureIds = lectureItems.stream().map(LectureItem::getLectureId).collect(Collectors.toList());
        List<Lecture> lectures = lectureRepository.getLecturesByIds(lectureIds);
        List<Long> userIds = lectures.stream().map(Lecture::getUserId).collect(Collectors.toList());
        List<User> users = userService.getUsersByIds(userIds);
        return applicationDetails.stream()
                .map(applicationDetail -> applicationDetailMapper.mapToDTO(applicationDetail, lectureItems, users, lectures))
                .collect(Collectors.toList());
    }

}
