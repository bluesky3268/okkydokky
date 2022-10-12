package com.hyunbenny.okkydokky.entity;

import com.hyunbenny.okkydokky.enums.AuthType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @Column(name = "USER_NO")
    private Long userNo;

    @NotNull
    @Column(name = "USER_ID", length = 20, unique = true)
    private String userId;

    @NotNull
    @Column(name = "PASSWD", length = 30)
    private String passwd;

    @NotNull
    @Column(name = "NICKNAME", length = 20, unique = true)
    private String nickname;

    @NotNull
    @Column(name = "EMAIL", length = 30, unique = true)
    private String email;

    @Column(name = "EMAIL_CONFIRM", length = 1)
    private String emailConfirm;

    @Column(name = "TEL", length = 11)
    private String tel;

    @Column(name = "POINT")
    private int point;

    @Column(name = "AUTH_TYPE")
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(name = "REPORT_CNT")
    private int reportCnt;

    @Column(name = "LOGIN_FAIL_CNT")
    private int loginFailCnt;

    @Column(name = "LOCK_YN")
    private String lockYn;

    @NotNull
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "DEL_YN")
    private String delYn;


    @Builder
    public Users(Long userNo, String userId, String passwd, String nickname, String email, String emailConfirm, String tel, int point, AuthType authType, int reportCnt, int loginFailCnt, String lockYn, String delYn) {
        this.userNo = userNo;
        this.userId = userId;
        this.passwd = passwd;
        this.nickname = nickname;
        this.email = email;
        if (emailConfirm == null) {
            this.emailConfirm = "N";
        } else {
            this.emailConfirm = emailConfirm;
        }
        this.tel = tel;
        this.point = point;
        if (authType == null) {
            this.authType = AuthType.USER;
        }else{
            this.authType = authType;
        }
        this.reportCnt = reportCnt;
        this.loginFailCnt = loginFailCnt;
        if (lockYn == null) {
            this.lockYn = "N";
        }else{
            this.lockYn = lockYn;
        }
        this.regDate = LocalDateTime.now();
        if (delYn == null) {
            this.delYn = "N";
        } else {
            this.delYn = delYn;
        }
    }

    public void updatePasswd(String passwd) {
        this.passwd = passwd;
    }

    public void updateTel(String tel) {
        this.tel = tel;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", passwd='" + passwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", emailConfirm='" + emailConfirm + '\'' +
                ", tel='" + tel + '\'' +
                ", point=" + point +
                ", authType=" + authType +
                ", reportCnt=" + reportCnt +
                ", loginFailCnt=" + loginFailCnt +
                ", lockYn='" + lockYn + '\'' +
                ", regDate=" + regDate +
                ", delYn='" + delYn + '\'' +
                '}';
    }
}
