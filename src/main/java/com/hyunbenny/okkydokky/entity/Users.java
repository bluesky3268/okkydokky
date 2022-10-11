package com.hyunbenny.okkydokky.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "USER_ID")
    private String userId;


    @Builder
    public Users(Long userNo, String userId) {
        this.userNo = userNo;
        this.userId = userId;
    }

}
