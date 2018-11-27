package com.november.model;

/**
 * @author skrT
 * @create 2018/11/23 20:17
 */
public interface CacheType {

    //  集合键前缀
    String LIST_PREFIX = "list_";

    //  是否需要马上更新前缀
    String NEEDUPDATE_PREFIX = "need_update_";

    // 普通String数据类型前缀
    String STRING_PREFIX = "string_";

    String OBJECT_PREFIX="object_";
}
