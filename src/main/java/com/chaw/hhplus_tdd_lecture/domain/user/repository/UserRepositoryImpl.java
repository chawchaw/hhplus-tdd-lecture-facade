package com.chaw.hhplus_tdd_lecture.domain.user.repository;

import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import com.chaw.hhplus_tdd_lecture.infrastructure.user.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userRepository;

    public UserRepositoryImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
