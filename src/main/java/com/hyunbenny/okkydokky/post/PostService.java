package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.post.dto.PostSaveReqDto;
import com.hyunbenny.okkydokky.post.dto.respDto.PostRespDto;
import com.hyunbenny.okkydokky.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public void savePost(PostSaveReqDto saveReqDto) throws IllegalArgumentException, Exception {

        Users findUser = userRepository.findByUserId(saveReqDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저정보입니다. userId : " +  saveReqDto.getUserId()));

        postRepository.save(saveReqDto.toEntity(findUser));
    }

    public PostRespDto getPost(Long postNo) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        return new PostRespDto().toPostRespDto(findPost);
    }
}
