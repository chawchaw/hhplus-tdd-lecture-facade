package com.chaw.hhplus_tdd_lecture.domain.user.repository;

import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface UserRepository {
    User findById(Long userId) throws EntityNotFoundException;
    List<User> findAllByIds(List<Long> userIds);

    User save(User user);

    void saveAll(List<User> users);

    void deleteById(Long userId);

    void deleteByIds(List<Long> userIds);
}
