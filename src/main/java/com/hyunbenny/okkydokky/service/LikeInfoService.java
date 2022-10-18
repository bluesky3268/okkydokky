package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.dto.post.respDto.PostRespDto;
import com.hyunbenny.okkydokky.entity.LikeInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.exception.UserNotExistException;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.hyunbenny.okkydokky.common.code.LikeStatus.DISLIKE;
import static com.hyunbenny.okkydokky.common.code.LikeStatus.LIKE;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeInfoService {

    private final LikeInfoRepository likeInfoRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeInfo findByPostNoAndUserId(Long postNo, String userId) {
        postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        Users findUser = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotExistException(userId));
        return likeInfoRepository.findByPostNoAndUserNo(postNo, findUser.getUserNo());
    }

    @Transactional
    public PostRespDto hitLikeBtn(Long postNo, String hitLikeBtnUserId) {
        Post findPost = postRepository.findById(postNo).orElseThrow(() -> new PostNotFoundException());
        Users findUser = userRepository.findByUserId(hitLikeBtnUserId).orElseThrow(() -> new UserNotExistException(hitLikeBtnUserId));

        LikeInfo findLikeInfo = likeInfoRepository.findByPostNoAndUserNo(postNo, findUser.getUserNo());
        if (findLikeInfo == null) {
            likeInfoRepository.save(LikeInfo.builder()
                    .postNo(findPost.getPostNo())
                    .userNo(findUser.getUserNo())
                    .status(LIKE)
                    .build());
            turnOnLikeBtn(findPost);
        }else if(findLikeInfo.getStatus() == LIKE){
            likeInfoRepository.deleteById(findLikeInfo.getLikeInfoNo());
            turnOffLikeBtn(findPost);
        }else {
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
        Users findUser = userRepository.findByUserId(hitDislikeBtnUserId).orElseThrow(() -> new UserNotExistException(hitDislikeBtnUserId));

        LikeInfo findLikeInfo = likeInfoRepository.findByPostNoAndUserNo(postNo, findUser.getUserNo());

        if (findLikeInfo == null) {
            likeInfoRepository.save(LikeInfo.builder()
                    .postNo(postNo)
                    .userNo(findUser.getUserNo())
                    .status(DISLIKE)
                    .build());

            turnOnDislikeBtn(findPost);
        } else if (findLikeInfo.getStatus() == DISLIKE) {
            likeInfoRepository.deleteById(findLikeInfo.getLikeInfoNo());
            turnOffDislikeBtn(findPost);
        }else{
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
