package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
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

    @Transactional(rollbackFor = Exception.class)
    public void savePost(PostSaveReqDto saveReqDto) {

        Users findUser = userRepository.findByUserId(saveReqDto.getUserId());

        postRepository.save(saveReqDto.toEntity(findUser));

    }
}
