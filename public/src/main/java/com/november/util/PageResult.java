package com.november.util;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
//  分页返回结果
public class PageResult<T> {

    //  当前页
    private int pageNo = 1;

    //  每页大小
    private int pageSize = 10;

    //  数据
    private List<T> data = Lists.newArrayList();

    //  记录数
    private int total = 0;
}
