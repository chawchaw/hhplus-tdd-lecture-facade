package com.chaw.hhplus_tdd_lecture.infrastructure.user;

import com.chaw.hhplus_tdd_lecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

}
