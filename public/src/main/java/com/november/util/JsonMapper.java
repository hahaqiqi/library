package com.november.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

/**
 * @author skrT
 * @create 2018/10/29 17:24
 */
//  转换json工具
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        //config
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        objectMapper.setSerializationConfig(objectMapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")));
        objectMapper.setDeserializationConfig(objectMapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")));
    }

    public static <T> String obj2String(T src){
        if(src == null){
            return null;
        }
        try{
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        }catch (Exception e){
            log.warn("parse object to String exception,error:{}",e);
            return null;
        }
    }

    public static <T> T string2Obj(String src,TypeReference<T> typeReference){
        if(src == null || typeReference == null){
            return null;
        }
        try{
            return (T) (typeReference.getType().equals(String.class)?src:objectMapper.readValue(src,typeReference));
        }catch (Exception e){
            log.warn("String to Object exception ,String:{},TypeReference<T>:{},error:{}",src,typeReference.getType(),e);
            return null;
        }
    }

    public static <T> T string2Obj(String src,Class<T> clazz){
        if(src == null || clazz == null){
            return null;
        }
        try{
            return (T) objectMapper.readValue(src,clazz);
        }catch (Exception e){
            log.warn("String to Object exception ,String:{},Class<T>:{},error:{}",src,clazz,e);
            return null;
        }
    }

}
