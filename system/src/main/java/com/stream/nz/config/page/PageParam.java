package com.stream.nz.config.page;

import lombok.Data;

@Data
public class PageParam {
    private Integer pageNum;
    private Integer pageSize;

    public static PageParam create() {
        return new PageParam();
    }

    public PageParam pageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageParam pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
