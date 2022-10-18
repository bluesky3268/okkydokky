package com.hyunbenny.okkydokky.repository.comments;

import com.hyunbenny.okkydokky.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsRepositoryCustom{
}
