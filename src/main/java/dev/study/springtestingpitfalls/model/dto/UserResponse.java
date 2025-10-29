package dev.study.springtestingpitfalls.model.dto;

import dev.study.springtestingpitfalls.model.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private UserStatus status;
    private Long lastLoginAt;
}
