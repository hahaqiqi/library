package com.november.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author skrT
 * @create 2018/10/29 13:19
 */
//  返回数据皆用该对象包装
@Getter
@Setter
public class JsonData {
    /**
     *  消息
     * */
    private String msg;
    /**
     *  返回是否成功
     * */
    private boolean ret;
    /**
     *  数据
     * */
    private Object data;

    public JsonData(boolean ret){
        this.ret = ret;
    }

    public static JsonData success(Object data,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static JsonData success(Object data){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        return jsonData;
    }

    public static JsonData success(){
        return new JsonData(true);
    }

    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("msg",this.msg);
        map.put("ret",this.ret);
        map.put("data",this.data);
        return map;
    }

}
