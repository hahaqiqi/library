package com.november.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author skrT
 * @create 2018/10/30 10:48
 */

//  可获取ioc容器中组件
@Component(value = "applicationContextHelper")
@Lazy(value = false)
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T propBean(Class<T> clazz){
        if(null==applicationContext){
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static <T> T propBean(String beanName,Class<T> clazz){
        if(null==applicationContext){
            return null;
        }
        return applicationContext.getBean(beanName,clazz);
    }
}
