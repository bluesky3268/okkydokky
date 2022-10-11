package com.hyunbenny.okkydokky.entity;

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
    @Column(name = "USER_ID", length = 20)
    private String userId;

    @NotNull
    @Column(name = "PASSWD", length = 30)
    private String passwd;

    @NotNull
    @Column(name = "NICKNAME", length = 20)
    private String nickname;

    @NotNull
    @Column(name = "EMAIL", length = 30)
    private String email;

    @Column(name = "EMAIL_CONFIRM", length = 1, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String emailConfirm;

    @Column(name = "TEL", length = 11)
    private String tel;

    @OneToOne
    @JoinColumn(name = "FILE_NO")
    private File file;

    @Column(name = "POINT")
    private Integer point;

    @Column(name = "AUTH_TYPE",  columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private AuthType authType;

    @Column(name = "REPORT_CNT")
    private Integer reportCnt;

    @Column(name = "LOGIN_FAIL_CNT")
    private Integer loginFailCnt;

    @Column(name = "LOCK_YN", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String lockYn;

    @NotNull
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "DEL_YN", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String delYn;


    @Builder
    public Users(Long userNo, String userId, String passwd, String nickname, String email, String emailConfirm, String tel, File file, Integer point, AuthType authType, Integer reportCnt, Integer loginFailCnt, String lockYn, LocalDateTime regDate, String delYn) {
        this.userNo = userNo;
        this.userId = userId;
        this.passwd = passwd;
        this.nickname = nickname;
        this.email = email;
        this.emailConfirm = emailConfirm;
        this.tel = tel;
        this.file = file;
        this.point = point;
        this.authType = authType;
        this.reportCnt = reportCnt;
        this.loginFailCnt = loginFailCnt;
        this.lockYn = lockYn;
        this.regDate = LocalDateTime.now();
        this.delYn = delYn;
    }
}
