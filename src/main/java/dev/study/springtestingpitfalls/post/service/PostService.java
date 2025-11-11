package dev.study.springtestingpitfalls.post.service;

import dev.study.springtestingpitfalls.common.domain.exception.ResourceNotFoundException;
import dev.study.springtestingpitfalls.post.domain.PostCreate;
import dev.study.springtestingpitfalls.post.domain.PostUpdate;
import dev.study.springtestingpitfalls.post.infrastructure.PostEntity;
import dev.study.springtestingpitfalls.post.service.port.PostRepository;
import dev.study.springtestingpitfalls.user.infrastructure.UserEntity;
import dev.study.springtestingpitfalls.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity create(PostCreate postCreate) {
        UserEntity userEntity = userService.getById(postCreate.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreate.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity update(long id, PostUpdate postUpdate) {
        PostEntity postEntity = getById(id);
        postEntity.setContent(postUpdate.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}