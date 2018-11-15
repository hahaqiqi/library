package com.november.util;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
//  分页查询条件收集
public class PageQuery {

    //  当前页
    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;

    //  每页大小
    @Getter
    @Setter
    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;

    //  实际用于sql中的页
    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
