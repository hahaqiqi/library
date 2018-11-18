package com.november.book.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class BookLossTypeParam {
    private Integer id;

    @NotBlank(message = "损坏类型名称不能为空")
    @Length(max = 20,message = "损坏类型名称长度不能超过20")
    private String typeName;

    @Min(value = 0,message = "扣除的积分必须大于0")
    private Integer score=0;

    @Min(value = 0,message = "扣除的钱必须大于0")
    private Integer price=0;

    @Length(max = 200,message = "备注长度不能超过200")
    private String remark;

}