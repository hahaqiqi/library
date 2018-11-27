package com.november.log.Param;

import lombok.*;

/**
 * @author skrT
 * @create 2018/11/27 10:46
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogParam {

    //  操作类型,对应LogType
    private int type;
    //  操作对象id
    private int targetId;
    //  操作前的值
    private String oldValue;
    //  操作后的值
    private String newValue;
    //  备注
    private String remark;
}
