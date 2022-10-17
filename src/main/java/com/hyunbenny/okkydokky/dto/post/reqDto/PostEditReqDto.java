package com.hyunbenny.okkydokky.dto.post.reqDto;

import com.hyunbenny.okkydokky.enums.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEditReqDto {

    private Long postNo;

    private BoardType boardType;

    private String title;

    private String cont;

    @Builder
    public PostEditReqDto(Long postNo, BoardType boardType, String title, String cont) {
        this.postNo = postNo;
        this.boardType = boardType;
        this.title = title;
        this.cont = cont;
    }

    @Override
    public String toString() {
        return "PostEditReqDto{" +
                "postNo=" + postNo +
                ", boardType=" + boardType +
                ", title='" + title + '\'' +
                ", cont='" + cont + '\'' +
                '}';
    }
}
