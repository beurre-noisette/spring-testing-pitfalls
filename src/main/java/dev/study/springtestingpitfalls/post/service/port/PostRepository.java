package dev.study.springtestingpitfalls.post.service.port;

import dev.study.springtestingpitfalls.post.infrastructure.PostEntity;

import java.util.Optional;

public interface PostRepository {

    Optional<PostEntity> findById(long id);

    PostEntity save(PostEntity postEntity);
}
