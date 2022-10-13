package com.hyunbenny.okkydokky.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
public class PostPager {

    private final Integer MAXSIZE = 100;

    private final Integer MINSIZE = 10;

    private int page;

    private int size;

    private Sort.Direction direction = Sort.Direction.DESC;

    @Builder
    public PostPager(int page, int size) {
        this.page = page <= 0 ? 0 : page -1;
        this.size = size <= 0 ? MINSIZE : Math.min(size, MAXSIZE);
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of(String column) {
        return org.springframework.data.domain.PageRequest.of(page, size, direction, column);
    }

//    public long getOffset() {
//        return (Math.max(1, page)-1) * Math.min(size, MAXSIZE);
//    }

}
