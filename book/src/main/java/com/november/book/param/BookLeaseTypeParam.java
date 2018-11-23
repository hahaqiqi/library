package com.november.book.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class BookLeaseTypeParam {
    private Integer id;

    @NotBlank(message = "名称不能为空")
    @Length(max = 20,message = "名称长度不能超过20")
    private String typeName;

    @Min(value = 0,message = "所需积分必须大于0")
    @NotNull(message = "所需积分不能为空")
    private Integer score;

    @NotNull(message = "折扣不能为空")
    private Double discount;

    @Length(max = 200,message = "备注长度不能超过200")
    private String remark;

}