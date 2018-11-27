package com.november.acl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author skrT
 * @create 2018/11/26 10:01
 */
@Getter
@Setter
@ToString
public class OperType implements Serializable {

    private int id;

    private String name;
}
