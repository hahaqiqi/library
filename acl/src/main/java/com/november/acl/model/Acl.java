package com.november.acl.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Acl {
    private Integer id;

    private String aclName;

    private Integer parentId;

    private Integer status;

    private Integer seq;

    private String url;

    private String operator;

    private Date operateTime;

    private String remark;

    private List<Acl> list;

}