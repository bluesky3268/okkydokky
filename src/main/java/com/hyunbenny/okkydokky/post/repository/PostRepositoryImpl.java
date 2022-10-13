package com.hyunbenny.okkydokky.post.repository;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.QPost;
import com.hyunbenny.okkydokky.entity.QUsers;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllPostsWithPaging(BoardType boardType, Pageable pageable) {
        QPost post = QPost.post;
        QUsers user = QUsers.users;

        List<Post> result = queryFactory.select(post)
                .from(post)
                .leftJoin(user).on(user.userNo.eq(post.user.userNo))
                .where(post.boardType.eq(boardType))
                .orderBy(post.postNo.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }
}
