package com.hyunbenny.okkydokky.repository.comments;

import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.entity.QComments;
import com.hyunbenny.okkydokky.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CommentsRepositoryImpl implements CommentsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QPost post = QPost.post;
    QComments comment = QComments.comments;

    // 최상위 부모 댓글만 조회
    @Override
    public List<Comments> findAllByPostNo(Long postNo, Pageable pageable) {
        return  queryFactory.selectFrom(comment)
                .where(post.postNo.eq(postNo).and(comment.parentComm.isNull()))
                .orderBy(comment.commentNo.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    // 자식 댓글 조회
    @Override
    public List<Comments> findAllChildComments(Long postNo) {
        return  queryFactory.selectFrom(comment)
                .where(post.postNo.eq(postNo).and(comment.parentComm.isNotNull()))
                .orderBy(comment.commentNo.asc())
                .fetch();
    }
}
