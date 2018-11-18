package com.november.book.param;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


@Getter
@Setter
@ToString
public class BookTypeParam {

    private Integer id;

    @NotBlank(message = "图书类型不能为空")
    @Length(min = 2,max = 20,message = "图书类型长度应在2-20个字之间")
    private String typeName;


    @Length(max = 200,message = "备注长度不能超过200")
    private String remark;
}
