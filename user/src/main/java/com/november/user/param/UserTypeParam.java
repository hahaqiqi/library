package com.november.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
public class UserTypeParam implements Serializable{
    private Integer id;

    @NotBlank(message = "用户类型名称不能为空")
    private String typeName;

    @NotNull(message = "用户类型最小积分不能为空")
    private Integer minScore;


    @Min(value =0 ,message ="折扣最低为0" )
    private Double discount;

    private Integer status;

    private String operator;

    private Date operateTime;

    private String remark;
}
