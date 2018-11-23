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
public class BookParam {
    private Integer id;

    @NotBlank(message = "书籍名称不能为空")
    @Length(max = 40,message = "书籍名称长度不能超过40")
    private String bookName;

    private String bookCode;

    @NotBlank(message = "作者名字不能为空")
    @Length(max = 20,message = "作者名字长度不能超过20")
    private String authorName;

    @Min(value = 0,message = "价格必须大于0")
    @NotNull(message = "价格不能为空")
    private Double price;

    @NotBlank(message = "出版社不能为空")
    @Length(max = 20,message = "出版社长度不能超过20")
    private String pressName;

    private Integer status;

    @NotNull(message = "图书类型不能为空")
    private Integer bookTypeId;

    private Integer bookLeaseId;

    private Integer bookLossId;

    private Integer bookLeaseType;

    private Integer bookChcoType;

    private Integer bookSpaceId;

    @Length(max = 200,message = "备注长度不能超过200")
    private String remark;

    @Min(value = 1,message = "书籍数量必须大于1")
    @NotNull(message = "数量不能为空")
    private Integer count;

}