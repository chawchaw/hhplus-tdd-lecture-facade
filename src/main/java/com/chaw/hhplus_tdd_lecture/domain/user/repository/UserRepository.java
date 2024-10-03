package com.chaw.hhplus_tdd_lecture.domain.user.repository;

import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAllByIds(List<Long> userIds);

    User save(User user);
}
