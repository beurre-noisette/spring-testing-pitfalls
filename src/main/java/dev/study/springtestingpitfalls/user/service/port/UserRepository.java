package dev.study.springtestingpitfalls.user.service.port;

import dev.study.springtestingpitfalls.user.domain.UserStatus;
import dev.study.springtestingpitfalls.user.infrastructure.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(long id);
}
