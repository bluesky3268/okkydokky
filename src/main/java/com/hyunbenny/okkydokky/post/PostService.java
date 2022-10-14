package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.exception.UserNotExistException;
import com.hyunbenny.okkydokky.post.dto.reqDto.PostSaveReqDto;
import com.hyunbenny.okkydokky.post.dto.reqDto.PostEditReqDto;
import com.hyunbenny.okkydokky.post.dto.respDto.PostListRespDto;
import com.hyunbenny.okkydokky.post.dto.respDto.PostRespDto;
import com.hyunbenny.okkydokky.post.repository.PostRepository;
import com.hyunbenny.okkydokky.users.UserRepository;
import com.hyunbenny.okkydokky.util.PostPager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    // 게시글 등록
    public void savePost(PostSaveReqDto saveReqDto) throws IllegalArgumentException, Exception {

        Users findUser = userRepository.findByUserId(saveReqDto.getUserId()).orElseThrow(() ->
                new UserNotExistException(saveReqDto.getUserId()));

        postRepository.save(saveReqDto.toEntity(findUser));
    }

    // 게시글 단건 조회
    @Transactional
    public PostRespDto getPost(Long postNo) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        findPost.increaseViews();
        return new PostRespDto().toPostRespDto(findPost);
    }

    // 게시글 목록 조회
    public Page<PostListRespDto> getPostList(BoardType boardType, PostPager pager) {
        Page<Post> result = postRepository.findAllPostsWithPaging(boardType, pager.of("POST_NO"));
        List<PostListRespDto> postList = result.getContent().stream().map(post -> new PostListRespDto().toListRespDto(post)).collect(Collectors.toList());
        return new PageImpl<>(postList, result.getPageable(), postList.size());
    }

    // 게시글 수정
    @Transactional
    public PostRespDto modifyPost(PostEditReqDto updateDto) {
        Post findPost = postRepository.findById(updateDto.getPostNo()).orElseThrow(() -> new PostNotFoundException());
        findPost.modifyPost(updateDto);
        return new PostRespDto().toPostRespDto(findPost);
    }

    // 게시글 삭제
    public void deletePost(Long postNo) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        postRepository.delete(findPost);
    }

    @Transactional
    public PostRespDto movePostToOtherBoard(Long postNo, BoardType moveBoardType) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        findPost.moveBoard(moveBoardType);
        return new PostRespDto().toPostRespDto(findPost);
    }

    @Transactional
    public PostRespDto hitLikeBtn(Long postNo) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        findPost.hitLikeBtn();
        return new PostRespDto().toPostRespDto(findPost);
    }

    @Transactional
    public PostRespDto hitDislikeBtn(Long postNo) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        findPost.hitDisLikeBtn();
        return new PostRespDto().toPostRespDto(findPost);
    }
}
