package com.november.space.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class SpaceParam {

    private Integer id;

    @NotBlank(message = "空间名称不可以为空")
    @Length(min = 2, max = 20, message = "空间名称长度需要在2-20个字之间")
    private String spaceName;

    private Integer parentId=-1;

    @Min(value = 0, message = "层级不合法")
    @Max(value = 2, message = "层级不合法")
    private String level = "1";


    @Length(min = 0, max = 200, message = "空间备注长度需要在200个字符以内")
    private String remark;
}
