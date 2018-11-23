package com.november.book.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class BookCodeParam {
    private Integer id;

    @Min(value = 0,message = "范围小值必须大于0")
    @NotNull(message = "值不能为空")
    private Integer bookPriceMin;

    private Integer bookPriceMax;

    @Min(value = 0,message = "租借价格必须大于等于0")
    @NotNull(message = "值不能为空")
    private Double bookPrice;

    @Length(max = 200,message = "备注长度不能超过200")
    private String remark;


}