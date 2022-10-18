package com.hyunbenny.okkydokky.repository.likeInfo;

import com.hyunbenny.okkydokky.entity.LikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LikeInfoRepository extends JpaRepository<LikeInfo, Long> {

    @Query("SELECT L FROM LikeInfo L WHERE L.postNo = ?1 AND L.userNo = ?2")
    LikeInfo findByPostNoAndUserNo(Long postNo, Long userNo);

}
