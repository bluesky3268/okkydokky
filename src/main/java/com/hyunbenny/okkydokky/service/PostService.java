package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.entity.LikeInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.exception.UserNotExistException;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.dto.post.reqDto.PostSaveReqDto;
import com.hyunbenny.okkydokky.dto.post.reqDto.PostEditReqDto;
import com.hyunbenny.okkydokky.dto.post.respDto.PostListRespDto;
import com.hyunbenny.okkydokky.dto.post.respDto.PostRespDto;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import com.hyunbenny.okkydokky.common.util.PostPager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.hyunbenny.okkydokky.common.code.LikeStatus.*;
import static com.hyunbenny.okkydokky.common.code.PointPolicy.ADD_POST;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final LikeInfoRepository likeInfoRepository;

    // 게시글 등록
    @Transactional
    public void savePost(PostSaveReqDto saveReqDto) {

        Users findUser = userRepository.findByUserId(saveReqDto.getUserId()).orElseThrow(() ->
                new UserNotExistException(saveReqDto.getUserId()));

        findUser.increasePoint(ADD_POST);
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

    // 게시글 다른 게시판으로 이동
    @Transactional
    public PostRespDto movePostToOtherBoard(Long postNo, BoardType moveBoardType) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        findPost.moveBoard(moveBoardType);
        return new PostRespDto().toPostRespDto(findPost);
    }

    // 추천
    @Transactional
    public PostRespDto hitLikeBtn(Long postNo, String hitLikeBtnUserId) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        Users findUser = userRepository.findByUserId(hitLikeBtnUserId).orElseThrow(() -> new UserNotExistException(hitLikeBtnUserId));

        //  likeInfo 조회 후
        LikeInfo findLikeInfo = likeInfoRepository.findByPostNoAndUserId(postNo, findUser.getUserNo());
        if (findLikeInfo == null) {
            log.info("LIKEBTN========== findLikeInfo == null");
            // 1-1.없으면 insert
            likeInfoRepository.save(LikeInfo.builder()
                    .postNo(findPost.getPostNo())
                    .userNo(findUser.getUserNo())
                    .status(LIKE)
                    .build());

            // 1-2.게시글의 추천 수 증가
            turnOnLikeBtn(findPost);
        }else if(findLikeInfo.getStatus() == LIKE){
            log.info("LIKEBTN========== findLikeInfo.getStatus() == LIKE");
            // 2-1. 이미 있으면 likeInfo delete
            likeInfoRepository.deleteById(findLikeInfo.getLikeInfoNo());
            // 2-2. 게시글 추천 수 하나 감소
            turnOffLikeBtn(findPost);
        }else {
            log.info("LIKEBTN========== findLikeInfo.getStatus() == DISLIKE");
            findLikeInfo.updateLikeStatus(LIKE);
            turnOffDislikeBtn(findPost);
            turnOnLikeBtn(findPost);
        }

        return new PostRespDto().toPostRespDto(findPost);
    }

    // 비추천
    @Transactional
    public PostRespDto hitDislikeBtn(Long postNo, String hitDislikeBtnUserId) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        Users findUser = userRepository.findByUserId(hitDislikeBtnUserId).get();

        LikeInfo findLikeInfo = likeInfoRepository.findByPostNoAndUserId(postNo, findUser.getUserNo());

        if (findLikeInfo == null) {
            log.info("DISLIKEBTN========== findLikeInfo IS NULL");
            likeInfoRepository.save(LikeInfo.builder()
                    .postNo(postNo)
                    .userNo(findUser.getUserNo())
                    .status(DISLIKE)
                    .build());

            turnOnDislikeBtn(findPost);
        } else if (findLikeInfo.getStatus() == DISLIKE) {
            log.info("DISLIKEBTN========== findLikeInfo.getStatus() == DISLIKE");

            likeInfoRepository.deleteById(findLikeInfo.getLikeInfoNo());
            turnOffDislikeBtn(findPost);
        }else{
            log.info("DISLIKEBTN========== findLikeInfo.getStatus() == LIKE");
            findLikeInfo.updateLikeStatus(DISLIKE);
            turnOffLikeBtn(findPost);
            turnOnDislikeBtn(findPost);
        }

        return new PostRespDto().toPostRespDto(findPost);
    }

    private void turnOnLikeBtn(Post post) {
        post.increaseLike();
    }

    private void turnOffLikeBtn(Post post) {
        post.decreaseLike();
    }

    private void turnOnDislikeBtn(Post post) {
        post.increaseDislike();
    }

    private void turnOffDislikeBtn(Post post) {
        post.decreaseDislike();
    }
}
