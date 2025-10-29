package dev.study.springtestingpitfalls.service;

import dev.study.springtestingpitfalls.exception.ResourceNotFoundException;
import dev.study.springtestingpitfalls.model.dto.PostCreateDto;
import dev.study.springtestingpitfalls.model.dto.PostUpdateDto;
import dev.study.springtestingpitfalls.repository.PostEntity;
import dev.study.springtestingpitfalls.repository.PostRepository;
import dev.study.springtestingpitfalls.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity createPost(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getByIdOrElseThrow(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity updatePost(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getPostById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}