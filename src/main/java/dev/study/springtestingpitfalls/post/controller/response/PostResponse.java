package dev.study.springtestingpitfalls.post.controller.response;

import dev.study.springtestingpitfalls.user.controller.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;
}
