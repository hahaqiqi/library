package com.november.common;

import com.november.model.CodeEnum;
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

    private Integer code;

    private Integer count;

    public JsonData(boolean ret){
        this.ret = ret;
    }

    public static JsonData success(Object data,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        jsonData.setMsg(msg);
        jsonData.setCode(CodeEnum.SUCCESS);
        return jsonData;
    }

    public static JsonData success(Object data){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        jsonData.setCode(CodeEnum.SUCCESS);
        return jsonData;
    }

    public static JsonData pageSuccess(Object data,Integer count){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        jsonData.setCount(count);
        jsonData.setCode(CodeEnum.SUCCESS);
        return jsonData;
    }

    public static JsonData success(){
        return new JsonData(true);
    }

    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.setMsg(msg);
        jsonData.setCode(CodeEnum.FAIL);
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("msg",this.msg);
        map.put("ret",this.ret);
        map.put("code",this.code);
        map.put("data",this.data);
        return map;
    }

}
