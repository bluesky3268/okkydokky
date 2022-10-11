package com.hyunbenny.okkydokky.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class File {

    @Id
    @Column(name = "FILE_NO")
    private Long fileNo;

    @Column(name = "FILE_SEQ")
    private Integer fileSeq;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "ORIGIN_NAME")
    private String originName;

    @Column(name = "FILE_EXT")
    private String fileExt;

    @Column(name = "FILE_SIZE")
    private String fileSize;

    @Column(name = "DEL_YN")
    private String delYn;

    @Column(name = "REG_USER")
    private String regUser;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Builder
    public File(Long fileNo, Integer fileSeq, String fileName, String originName, String fileExt, String fileSize, String delYn, String regUser, LocalDateTime regDate) {
        this.fileNo = fileNo;
        this.fileSeq = fileSeq;
        this.fileName = fileName;
        this.originName = originName;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.delYn = delYn;
        this.regUser = regUser;
        this.regDate = regDate;
    }
}
