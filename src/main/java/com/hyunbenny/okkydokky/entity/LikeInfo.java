package com.hyunbenny.okkydokky.entity;

import com.hyunbenny.okkydokky.common.code.LikeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class LikeInfo {

    @Id
    @Column(name = "LIKE_INFO_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeInfoNo;

//    @ManyToOne
//    @JoinColumn(name = "USER_NO")
//    private Users user;
//
//    @ManyToOne
//    @JoinColumn(name = "POST_NO")
//    private Post post;

    @Column(name = "POST_NO")
    private Long postNo;

    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public LikeInfo(Long likeInfoNo, Long postNo, Long userNo, LikeStatus status) {
        this.likeInfoNo = likeInfoNo;
        this.postNo = postNo;
        this.userNo = userNo;
        this.status = status;
    }

    @Override
    public String toString() {
        return "LikeInfo{" +
                "likeInfoNo=" + likeInfoNo +
                ", postNo=" + postNo +
                ", userNo=" + userNo +
                ", status='" + status + '\'' +
                '}';
    }
}
